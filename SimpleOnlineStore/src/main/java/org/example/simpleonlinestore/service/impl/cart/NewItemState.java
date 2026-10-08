package org.example.simpleonlinestore.service.impl.cart;

import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.CartItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.service.interfaces.CartItemState;

public class NewItemState implements CartItemState {

    @Override
    public int getCurrentQuantity() {
        return 0;
    }

    @Override
    public void applyChange(Cart cart, Product product, int requestedQuantity) {
        CartItem cartItem=CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(requestedQuantity)
                .build();
        cart.getItems().add(cartItem);
    }
}
