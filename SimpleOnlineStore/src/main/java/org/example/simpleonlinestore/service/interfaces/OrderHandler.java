package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.service.impl.order.OrderContext;

public interface OrderHandler {
    void handle(OrderContext context);
}