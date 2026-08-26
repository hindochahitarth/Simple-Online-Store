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
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items/{productId}/{quantity}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId,@PathVariable Long productId,@PathVariable Integer quantity){
        return ResponseEntity.ok(
                cartService.addToCart(
                        userId,
                        productId,
                        quantity
                )
        );
    }
    @DeleteMapping("/delete/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long userId,@PathVariable Long productId){
        Cart updatedCart=cartService.removeFromCart(userId,productId);
        return ResponseEntity.ok(updatedCart);
    }

}
