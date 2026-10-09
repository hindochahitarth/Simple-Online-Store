package org.example.simpleonlinestore.service.impl.cart;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.CartItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.CartItemState;
import org.example.simpleonlinestore.service.interfaces.CartService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

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

        CartItemState state=cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .<CartItemState> map(ExistingItemState::new)
                .orElseGet(NewItemState::new);
//        Optional<CartItem> existItem=cart.getItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst();
//        int newQuant=quantity;
//        if(existItem.isPresent()){
//            newQuant+=existItem.get().getQuantity();
//        }
        int newQuant=state.getCurrentQuantity()+quantity;
        log.info("product.getStockCount()"+product.getStockCount()+" new quant"+newQuant);

        if(newQuant > product.getStockCount()){
            throw new RuntimeException("Insufficient Stock ");
        }
        state.applyChange(cart,product,newQuant);
//        if(existItem.isPresent()){
//            CartItem existingItem=existItem.get();
//            existingItem.setQuantity(newQuant);
//        }
//        else{
//            // ----- use builder pattern from here to build order
//            CartItem cartItem= CartItem.builder()
//                    .cart(cart)
//                    .product(product)
//                    .quantity(quantity)
//                    .build();
//            cart.getItems().add(cartItem);
//        }
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
