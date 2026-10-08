package org.example.simpleonlinestore.service.impl.order;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.entity.OrderItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.enums.OrderStatus;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.OrderState;

public class PlacedState implements OrderState {

    @Override
    public void paymentSuccess(Order order, String paymentId) {
        throw new IllegalStateException("Order is already paid and placed");
    }

    @Override
    public void paymentFailed(Order order, ProductRepository productRepository) {
        throw new IllegalStateException("Cannot fail payment on an already placed order");
    }

    @Override
    public void cancelOrder(Order order, ProductRepository productRepository) {
        order.setStatus(OrderStatus.CANCELED);
        for(OrderItem item:order.getItems()){
            Product product=item.getProduct();
            product.setStockCount(product.getStockCount()+item.getQuantity());
            productRepository.save(product);
        }
        order.setState(new CancelledState());

    }
}
