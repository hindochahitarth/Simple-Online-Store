package org.example.simpleonlinestore.controller;

import lombok.Getter;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")

public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService=cartService;
    }
    @GetMapping("/get-cart/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add-to-cart/{userId}/items/{productId}/{quantity}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId,@PathVariable Long productId,@PathVariable Integer quantity){
        return ResponseEntity.ok(
                cartService.addToCart(
                        userId,
                        productId,
                        quantity
                )
        );
    }
    @PutMapping("/update-cart-item/{userId}/items/{productId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long userId,@PathVariable Long productId, @RequestParam int quantity){
        Cart updatedCart=cartService.updateCartItem(userId,productId,quantity);
        return ResponseEntity.ok(updatedCart);
    }
    @DeleteMapping("/remove-from-cart/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long userId,@PathVariable Long productId){
        Cart updatedCart=cartService.removeFromCart(userId,productId);
        return ResponseEntity.ok(updatedCart);
    }

}
