package org.example.service.interfaces;
import org.example.entity.Cart;

public interface CartService {
    Cart getCart();
    Cart addToCart(Long productId, Integer quantity);
    Cart removeFromCart(Long productId);
    Cart updateCartItem(Long productId, int newQuantity);
    Cart clearCart();
}

