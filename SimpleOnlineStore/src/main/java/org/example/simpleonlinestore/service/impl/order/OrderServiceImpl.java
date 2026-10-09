package org.example.simpleonlinestore.service.impl.order;
import org.example.simpleonlinestore.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.entity.*;
import org.example.simpleonlinestore.enums.OrderStatus;
import org.example.simpleonlinestore.service.impl.auth.EmailServiceImpl;
import org.example.simpleonlinestore.service.interfaces.InvoiceGenerator;
import org.example.simpleonlinestore.service.interfaces.OrderHandler;
import org.example.simpleonlinestore.service.interfaces.OrderService;
import org.example.simpleonlinestore.service.interfaces.PaymentStrategy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final List<PaymentStrategy> paymentStrategies;
    private final List<OrderHandler> orderHandlers;
     public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, RazorpayServiceImpl razorpayService, List<PaymentStrategy> paymentStrategies, AddressRepository addressRepository, EmailServiceImpl emailService, List<OrderHandler> orderHandlers){
        this.orderRepository=orderRepository;
        this.productRepository=productRepository;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
        this.paymentStrategies = paymentStrategies;
        this.orderHandlers = orderHandlers;
     }
    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
    @Transactional
    public Order placeOrder(Long addressId){
         OrderContext orderContext= OrderContext.builder()
                 .addressId(addressId)
                 .user(getLoggedInUser())
                 .build();
         for(OrderHandler handler:orderHandlers){
             handler.handle(orderContext);
         }
        Order order=Order.builder()
                .user(orderContext.getUser())
                .status(OrderStatus.PAYMENT_PENDING)
                .state(new PaymentPendingState())
                .address(orderContext.getAddress())
                .items(orderContext.getOrderItems())
                .totalAmount(orderContext.getTotalAmount())
                .razorpayOrderId(orderContext.getRazorpayOrderId())
                .build();
        for (OrderItem item : orderContext.getOrderItems()) {
            item.setOrder(order);
        }
        Order savedOrder=orderRepository.save(order);
        orderContext.getCart().getItems().clear();
        cartRepository.save(orderContext.getCart());

        return savedOrder;
    }
    @Transactional
    public Order    verifyPaymentSignature(Map<String, String> payload) {
        Long orderId = Long.parseLong(payload.get("orderId"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order record not found"));
        PaymentStrategy strategy = paymentStrategies.stream()
                .filter(s -> s.getProviderName().equalsIgnoreCase("RAZORPAY"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment provider engine not found"));
        boolean isValid = strategy.verifySignature(payload);

        if (isValid) {
            order.getState().paymentSuccess(order,payload.get("razorpay"));
            return orderRepository.save(order);
        }
        else {
                order.getState().paymentFailed(order,productRepository);
                for (OrderItem item : order.getItems()) {
                    Product product = item.getProduct();
                    product.setStockCount(product.getStockCount()+item.getQuantity());
                    productRepository.save(product);
                }
            return orderRepository.save(order);
        }
    }
    public List<Order> getOrderByUser(){
        User user=getLoggedInUser();
        return orderRepository.findByUserId(user.getId());
    }
    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order id "+orderId+"does not exist"));
    }
    @Transactional
    public Order cancelOrder(Long orderId) {
        User user = getLoggedInUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized action: This order does not belong to you.");
        }
        order.getState().cancelOrder(order,productRepository);
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockCount(product.getStockCount() + item.getQuantity());
            productRepository.save(product);
        }
        return orderRepository.save(order);
    }
    @Override
    public String generateInvoiceSummary(Long orderId,String format) {
        Order order = getOrderById(orderId);
        InvoiceGenerator generator=new PlainTextInvoiceGenerator();

        if("TAX_PLAIN".equalsIgnoreCase(format)){
            generator=new TaxInvoiceDecorator(generator);
        }
        else if("HTML_EMAIL".equalsIgnoreCase(format)){
            generator=new HtmlInvoiceDecorator(new TaxInvoiceDecorator(new PlainTextInvoiceGenerator()));
        }
        return generator.generate(order);
    }
}