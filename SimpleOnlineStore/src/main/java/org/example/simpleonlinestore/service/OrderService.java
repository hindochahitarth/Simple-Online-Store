package org.example.simpleonlinestore.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.entity.*;
import org.example.simpleonlinestore.enums.OrderStatus;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.repository.OrderRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final RazorpayService razorpayService;
    public OrderService(OrderRepository orderRepository,UserRepository userRepository,ProductRepository productRepository,CartRepository cartRepository,RazorpayService razorpayService){
        this.orderRepository=orderRepository;
        this.productRepository=productRepository;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
        this.razorpayService=razorpayService;
    }
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
    @Transactional
    public Order placeOrder(){

        User user=getLoggedInUser();

        Cart cart=cartRepository.findByUserId(user.getId()).orElseThrow(()->new RuntimeException("Cart not found"));

        if(cart.getItems()==null || cart.getItems().isEmpty()){
            throw new RuntimeException("Cart is empty ");
        }
        Order order=new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        List<OrderItem> orderItemList=new ArrayList<>();
        BigDecimal totalAmount=BigDecimal.ZERO;
        //checking stocks
        for(CartItem cartItem:cart.getItems()){
            Product product=cartItem.getProduct();

            if(product.getStockCount()<cartItem.getQuantity()){
                throw new RuntimeException("Insufficient Stock ");
            }
            product.setStockCount(product.getStockCount()-cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem=new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            long calculatedPrice = product.getPrice();
            if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
                long discountAmount = (product.getPrice() * product.getDiscountPercentage()) / 100;
                calculatedPrice = product.getPrice() - discountAmount;
            }

            BigDecimal price=BigDecimal.valueOf(calculatedPrice);
            orderItem.setPrice(price);

            totalAmount=totalAmount.add(
                    price.multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    )
            );

            orderItemList.add(orderItem);
        }
        order.setItems(orderItemList);
        order.setTotalAmount(totalAmount);
        try {
            String receiptId = "txn_" + System.currentTimeMillis();
            // Convert BigDecimal to Double securely for your method signature
            Double doubleAmount = totalAmount.doubleValue();

            JSONObject razorpayOrderJson = razorpayService.createOrder(doubleAmount, receiptId);
            order.setRazorpayOrderId(razorpayOrderJson.getString("id"));
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to generate gateway token: " + e.getMessage());
        }


        Order savedOrder=orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;

    }
    @Transactional
    public Order verifyPaymentSignature(Map<String, String> payload) {
        Long orderId = Long.parseLong(payload.get("orderId"));
        String razorpayOrderId = payload.get("razorpayOrderId");
        String razorpayPaymentId = payload.get("razorpayPaymentId");
        String razorpaySignature = payload.get("razorpaySignature");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order record not found"));

        boolean isValid = razorpayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (isValid) {
            order.setStatus(OrderStatus.PLACED);
            order.setRazorpayPaymentId(razorpayPaymentId);
            return orderRepository.save(order);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            // Restock items back into product listings
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockCount(product.getStockCount() + item.getQuantity());
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

}
