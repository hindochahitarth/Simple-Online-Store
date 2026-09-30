package com.niyantras.simpleonlinestore.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.niyantras.simpleonlinestore.entity.Cart;
import com.niyantras.simpleonlinestore.entity.CartItem;
import com.niyantras.simpleonlinestore.entity.Product;
import com.niyantras.simpleonlinestore.entity.User;
import com.niyantras.simpleonlinestore.repository.CartRepository;
import com.niyantras.simpleonlinestore.repository.ProductRepository;
import com.niyantras.simpleonlinestore.repository.UserRepository;
import com.niyantras.simpleonlinestore.service.interfaces.CartService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,ProductRepository productRepository,UserRepository userRepository){
        this.cartRepository=cartRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    public Cart getCart(){
        User user = getLoggedInUser();
        return cartRepository.findByUserId(user.getId()).orElseThrow(() ->    new RuntimeException("USer does not exist"));

    }
    public Cart addToCart(Long productId,Integer quantity){
            if(quantity == null || quantity <=0){
                throw new RuntimeException("Quantity cannot be less than or equal to zero ");
            }
            Cart  cart=getCart();

        Product product=productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exist"));

        Optional<CartItem> existItem=cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        int newQuant=quantity;
        if(existItem.isPresent()){
            newQuant+=existItem.get().getQuantity();
        }
        if(newQuant > product.getStockCount()){
            throw new RuntimeException("Insufficient Stock ");
        }
        if(existItem.isPresent()){
            CartItem existingItem=existItem.get();
            existingItem.setQuantity(newQuant);
        }
        else{
            // ----- use builder pattern from here to build order
            CartItem cartItem=new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getItems().add(cartItem);
        }
        // ----- use builder pattern upto here to build order
       // product.setStockCount(product.getStockCount() - quantity);
        log.info("product.getStockCount()"+product.getStockCount());

        return cartRepository.save(cart);
    }
    public Cart removeFromCart(Long productId) {
        Cart cart = getCart()   ;
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exist"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if (cartItem != null){
           // product.setStockCount(product.getStockCount()+cartItem.getQuantity());
            productRepository.save(product);

            cart.getItems().remove(cartItem);
        }

    return cartRepository.save(cart);
    }
    public Cart updateCartItem(Long productId,int newQuantity){
        Cart cart=getCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exist"));

        CartItem updateItem=cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if(newQuantity <=0){
            cart.getItems().remove(updateItem);
        }else{
                updateItem.setQuantity(newQuantity);
        }
        if (updateItem != null){
           // product.setStockCount(product.getStockCount()-updateItem.getQuantity());
            productRepository.save(product);
        }
    return cartRepository.save(cart);

    }
    public Cart clearCart(){
        Cart cart=getCart();
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
