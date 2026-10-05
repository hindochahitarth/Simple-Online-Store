package org.example.simpleonlinestore.service.interfaces;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.repository.ProductRepository;

public interface OrderState {
    void paymentSuccess(Order order, String paymentId);
    void paymentFailed(Order order, ProductRepository productRepository);
    void cancelOrder(Order order, ProductRepository productRepository);
}
