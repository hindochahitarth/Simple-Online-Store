package org.example.simpleonlinestore.service;

import org.example.simpleonlinestore.entity.*;
import org.example.simpleonlinestore.enums.OrderStatus;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.repository.OrderRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository,UserRepository userRepository,ProductRepository productRepository,CartRepository cartRepository){
        this.orderRepository=orderRepository;
        this.productRepository=productRepository;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
    }

    public Order placeOrder(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User with id "+userId+" not found "));

        Cart cart=cartRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("Cart not found"));

        if(cart.getItems()==null){
            throw new RuntimeException("Cart is empty ");
        }
        Order order=new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);

        List<OrderItem> orderItemList=new ArrayList<>();
        BigDecimal totalAmount=BigDecimal.ZERO;

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

            BigDecimal price=BigDecimal.valueOf(product.getPrice());
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

        if(orderItemList.isEmpty()) {
            throw  new RuntimeException("There are no order Items");

        }
        Order savedOrder=orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;

    }
    public List<Order> getOrderByUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new RuntimeException("User does not exist");
        }
        return orderRepository.findByUserId(userId);
    }
    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order id "+orderId+"does not exist"));

    }

}
