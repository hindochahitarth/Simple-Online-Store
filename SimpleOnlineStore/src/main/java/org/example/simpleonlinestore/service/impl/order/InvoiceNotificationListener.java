package org.example.simpleonlinestore.service.impl.order;
import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.service.interfaces.EmailService;
import org.example.simpleonlinestore.service.interfaces.OrderService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InvoiceNotificationListener {

    private final OrderService orderService;
    private final EmailService emailService;

    public InvoiceNotificationListener(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }
    @EventListener
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        Long orderId = event.getOrderId();
        // Fetch the order and details
        Order order = orderService.getOrderById(orderId);
        String customerEmail = order.getUser().getEmailId();
        String summary = orderService.generateInvoiceSummary(orderId, "TAX_PLAIN");
        // Send the email
        emailService.sendNotification(customerEmail, summary);
    }
}