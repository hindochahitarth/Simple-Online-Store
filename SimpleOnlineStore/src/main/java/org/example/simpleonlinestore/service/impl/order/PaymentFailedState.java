package org.example.simpleonlinestore.service.impl.order;
import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.OrderState;

public class PaymentFailedState implements OrderState {

    @Override
    public void paymentSuccess(Order order, String paymentId) {
            throw new IllegalStateException("Cannot complete Payment" );
    }
    @Override
    public void paymentFailed(Order order, ProductRepository productRepository) {
            throw new IllegalStateException("Payment already been marked as failed ");
    }
    @Override
    public void cancelOrder(Order order, ProductRepository productRepository) {
            throw new IllegalStateException("Cannot cancel an order that has been failed");
    }
}