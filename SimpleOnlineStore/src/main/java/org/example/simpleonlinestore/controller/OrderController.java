package org.example.simpleonlinestore.controller;

import org.aspectj.weaver.ast.Or;
import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.entity.OrderItem;
import org.example.simpleonlinestore.repository.OrderItemRepository;
import org.example.simpleonlinestore.repository.OrderRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place-order/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @GetMapping("/get-orders/{userId}")
    public ResponseEntity <List<Order>> getOrder(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.getOrderByUser(userId));
    }
    @GetMapping("/get-orders-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}

