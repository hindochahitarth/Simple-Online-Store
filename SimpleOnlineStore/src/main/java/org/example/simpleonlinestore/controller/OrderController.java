package org.example.simpleonlinestore.controller;

import org.example.simpleonlinestore.DTO.OrderRequestDTO;
import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.service.impl.EmailServiceImpl;
import org.example.simpleonlinestore.service.impl.OrderPlacedEvent;
import org.example.simpleonlinestore.service.impl.OrderServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final EmailServiceImpl emailService;
    private final ApplicationEventPublisher applicationEventPublisher;
    public OrderController(OrderServiceImpl orderService, EmailServiceImpl emailService, ApplicationEventPublisher applicationEventPublisher) {
        this.orderService = orderService;
        this.emailService=emailService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/place-order")

    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequestDTO requestDTO) {
        if(requestDTO.getAddressId()==null)
        {
            throw new RuntimeException("Address is required ");
        }
        Order order = orderService.placeOrder(requestDTO.getAddressId());
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

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId){
        Order canceledOrder=orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceledOrder);
    }
    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<String> getInvoiceSummary(@PathVariable Long orderId,@RequestParam(defaultValue = "plain") String format){
        String invoice=orderService.generateInvoiceSummary(orderId,format);
        return ResponseEntity.ok(invoice);

    }
    @PostMapping("/{orderId}/send-invoice")
    public ResponseEntity<String> sendInvoice(@PathVariable Long orderId) {
        applicationEventPublisher.publishEvent(new OrderPlacedEvent(orderId));
        return ResponseEntity.ok("Invoice email sent successfully to ");
    }

}

