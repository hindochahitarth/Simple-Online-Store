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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/place-order")
    public ResponseEntity<Order> placeOrder() {
        Order order = orderService.placeOrder();
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/verify-payment")
    public ResponseEntity<Order> verifyPayment(@RequestBody Map<String, String> payload) {
        Order completedOrder = orderService.verifyPaymentSignature(payload);
        return ResponseEntity.ok(completedOrder);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/get-orders")
    public ResponseEntity <List<Order>> getOrder(){
        return ResponseEntity.ok(orderService.getOrderByUser());
    }
    
}

