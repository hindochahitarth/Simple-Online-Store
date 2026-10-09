package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.Product;

public interface CartItemState {
    int getCurrentQuantity();
    void applyChange(Cart cart, Product product, int requestedQuantity);
}