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
        int newQuant=state.getCurrentQuantity()+quantity;
        final int MAX_ITEM_LIMIT = 10;
        if (newQuant > MAX_ITEM_LIMIT) {
            throw new RuntimeException("Cannot add more than " + MAX_ITEM_LIMIT + " units of this item to the cart");
        }
        log.info("product.getStockCount()"+product.getStockCount()+" new quant"+newQuant);
        if(newQuant > product.getStockCount()){
            throw new RuntimeException("Insufficient Stock ");
        }
        state.applyChange(cart,product,newQuant);
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
        if(updateItem == null && newQuantity>0){
            throw new RuntimeException("Item does not exist in your cart");
        }
        if(newQuantity <=0){
            if(updateItem!=null) {
                cart.getItems().remove(updateItem);
            }
        }else{
            final int MAX_ITEM_LIMIT = 10;
            if (newQuantity > MAX_ITEM_LIMIT) {
                throw new RuntimeException("Cannot add more than " + MAX_ITEM_LIMIT + " units of this item to the cart");
            }
            // Validate stock count before updating
            if (newQuantity > product.getStockCount()) {
                throw new RuntimeException("Insufficient Stock ");
            }
            updateItem.setQuantity(newQuantity);
        }
    return cartRepository.save(cart);
    }
    public Cart clearCart(){
        Cart cart=getCart();
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
