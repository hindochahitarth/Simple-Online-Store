package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.OrderState;

public class CancelledState implements OrderState {

    @Override
    public void paymentSuccess(Order order, String paymentId) {
        throw new IllegalStateException("Cannot process payment. Order is already cancelled ");
    }

    @Override
    public void paymentFailed(Order order, ProductRepository productRepository) {
        throw new IllegalStateException("Cannot fail payment. Order is already cancelled ");
    }

    @Override
    public void cancelOrder(Order order, ProductRepository productRepository) {
        throw new IllegalStateException("This order is already cancelled ");

    }
}
