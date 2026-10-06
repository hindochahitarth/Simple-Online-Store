package org.example.simpleonlinestore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.service.interfaces.OrderHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class CartValidationHandler implements OrderHandler {
    private final CartRepository cartRepository;

    @Override
    public void handle(OrderContext context) {
        Cart cart = cartRepository.findByUserId(context.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        context.setCart(cart);
    }
}

