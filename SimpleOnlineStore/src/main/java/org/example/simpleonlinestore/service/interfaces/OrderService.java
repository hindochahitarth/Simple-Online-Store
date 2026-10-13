package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.entity.Order;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Order placeOrder(Long addressId);
    Order verifyPaymentSignature(Map<String, String> payload);
    List<Order> getOrderByUser();
    Order getOrderById(Long orderId);
    Order cancelOrder(Long orderId);
    String generateInvoiceSummary(Long orderId,String format);
    BigDecimal getTotalAmountAllOrders();
}