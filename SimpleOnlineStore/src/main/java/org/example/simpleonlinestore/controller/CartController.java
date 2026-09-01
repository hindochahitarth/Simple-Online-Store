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
    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/add-to-cart/items/{productId}/{quantity}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long productId,@PathVariable Integer quantity){
        return ResponseEntity.ok(
                cartService.addToCart(

                        productId,
                        quantity
                )
        );
    }
    @PutMapping("/update-cart-item/items/{productId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long productId, @RequestParam int quantity){
        Cart updatedCart=cartService.updateCartItem(productId,quantity);
        return ResponseEntity.ok(updatedCart);
    }
    @DeleteMapping("/remove-from-cart/items/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long productId){
        Cart updatedCart=cartService.removeFromCart(productId);
        return ResponseEntity.ok(updatedCart);
    }

}
