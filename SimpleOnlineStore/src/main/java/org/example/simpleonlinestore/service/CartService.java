package org.example.simpleonlinestore.service;

import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.UserRequestDTO;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.CartItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public CartService(CartRepository cartRepository,ProductRepository productRepository,UserRepository userRepositorya){
        this.cartRepository=cartRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }
    public Cart getCart(Long userId){
        return cartRepository.findById(userId).orElseThrow(() -> new RuntimeException("USer does not exist"));

    }
    public Cart addToCart(Long userId,Long productId,Integer quantity){
            if(quantity == null || quantity <=0){
                throw new RuntimeException("Quantity cannot be less than or equal to zero ");
            }
            Cart  cart=getCart(userId);

        Product product=productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exist"));

        if(quantity > product.getStockCount()){
            throw new RuntimeException("Insufficient Stock ");
        }
        CartItem cartItem=new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }
    public Cart removeFromCart(Long userId,Long productId){
        Cart cart=getCart(userId);

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
    return cartRepository.save(cart);
    }



}
