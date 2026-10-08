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
    private final RazorpayServiceImpl razorpayService;
    private final AddressRepository addressRepository;
    private final EmailServiceImpl emailService;
    private final List<OrderHandler> orderHandlers;
     public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, RazorpayServiceImpl razorpayService, AddressRepository addressRepository, EmailServiceImpl emailService, List<OrderHandler> orderHandlers){
        this.orderRepository=orderRepository;
        this.productRepository=productRepository;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
        this.razorpayService=razorpayService;
        this.addressRepository=addressRepository;
        this.emailService=emailService;
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
//        User user=getLoggedInUser();
//        Address address=addressRepository.findById(addressId).orElseThrow(() ->  new RuntimeException("Address Not Found"));
//
//        if(!address.getUser().getId().equals(user.getId())){
//            throw new RuntimeException("This address does not belong to you.");
//        }
//        Cart cart=cartRepository.findByUserId(user.getId()).orElseThrow(()->new RuntimeException("Cart not found"));
//
//        if(cart.getItems()==null || cart.getItems().isEmpty()){
//            throw new RuntimeException("Cart is empty ");
//        }
//
////        order.setUser(user);
////        order.setStatus(OrderStatus.PAYMENT_PENDING);
////        order.setAddress(address);
//
//        List<OrderItem> orderItemList=new ArrayList<>();
//        BigDecimal totalAmount=BigDecimal.ZERO;
//        //checking stocks
//        for(CartItem cartItem:cart.getItems()){
//            Product product=cartItem.getProduct();
//
//            if(product.getStockCount()<cartItem.getQuantity()){
//                throw new RuntimeException("Insufficient Stock ");
//            }
//            product.setStockCount(product.getStockCount()-cartItem.getQuantity());
//            productRepository.save(product);
//
//            long calculatedPrice = product.getPrice();
//            if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
//                long discountAmount = (product.getPrice() * product.getDiscountPercentage()) / 100;
//                calculatedPrice = product.getPrice() - discountAmount;
//            }
//
//            BigDecimal price=BigDecimal.valueOf(calculatedPrice);
//            OrderItem orderItem=OrderItem.builder()
//                    .product(product)
//                    .quantity(cartItem.getQuantity())
//                    .price(price)
//                    .build();
//           // orderItem.setPrice(price);
//
//            // ----- use builder pattern upto here to build order
//            totalAmount=totalAmount.add(
//                    price.multiply(
//                            BigDecimal.valueOf(cartItem.getQuantity())
//                    )
//            );
//    log.warn(String.valueOf(totalAmount));
//            orderItemList.add(orderItem);
//        }
//
//        log.warn(String.valueOf(totalAmount));
//
//        log.warn(String.valueOf(totalAmount));
////        order.setItems(orderItemList);
////        order.setTotalAmount(totalAmount);
//        String razorpayOrderId=null;
//        try {
//            String receiptId = "txn_" + System.currentTimeMillis();
//            Double doubleAmount = totalAmount.doubleValue();
//
//            JSONObject razorpayOrderJson = razorpayService.createOrder(doubleAmount, receiptId);
//            razorpayOrderId=razorpayOrderJson.getString("id");
//
//            //order.setRazorpayOrderId(razorpayOrderJson.getString("id"));
//        } catch (RazorpayException e) {
//            throw new RuntimeException("Failed to generate gateway token: " + e.getMessage());
//        }
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
        String razorpayOrderId = payload.get("razorpayOrderId");
        String razorpayPaymentId = payload.get("razorpayPaymentId");
        String razorpaySignature = payload.get("razorpaySignature");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order record not found"));

        boolean isValid = razorpayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (isValid) {

            order.getState().paymentSuccess(order,razorpayPaymentId);
            Order.builder()
                            .status(OrderStatus.PLACED)
                            .razorpayPaymentId(razorpayPaymentId)
                            .build();
//            order.setStatus(OrderStatus.PLACED);
//            order.setRazorpayPaymentId(razorpayPaymentId);
            return orderRepository.save(order);
        } else {
                order.getState().paymentFailed(order,productRepository);
            //    order.setStatus(OrderStatus.PAYMENT_FAILED);
                // Restock items back into product listings
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                Product.builder()
                                .stockCount(product.getStockCount()+item.getQuantity());
                //product.setStockCount(product.getStockCount() + item.getQuantity());
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
            generator=new HtmlInvoiceDecorator(new TaxInvoiceDecorator(generator));
        }
        return generator.generate(order);
    }


}
