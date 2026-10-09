package org.example.simpleonlinestore.service.impl.cart;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.CartItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.service.interfaces.CartItemState;

public class ExistingItemState implements CartItemState {
    private final CartItem existingItem;

    public ExistingItemState(CartItem existingItem) {
        this.existingItem = existingItem;
    }

    @Override
    public int getCurrentQuantity() {
        return existingItem.getQuantity();
    }

    @Override
    public void applyChange(Cart cart, Product product, int requestedQuantity) {
        existingItem.setQuantity(requestedQuantity);
    }
}
