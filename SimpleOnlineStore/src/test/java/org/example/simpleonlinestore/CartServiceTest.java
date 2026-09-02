package org.example.simpleonlinestore;

import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.CartItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.simpleonlinestore.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    // Declared to let Mockito initialize the service safely
    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;
    @BeforeEach
    void setupSecurityContext() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@test.com");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void testAddToCartQuantity() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(1L, 0); // Testing with a zero quantity
        });

        // Verify the exact error message your method throws
        assertEquals("Quantity cannot be less than or equal to zero ", exception.getMessage());
    }
    @Test
    void testAddToCartProductDoesNotExist() {
        // 1. Arrange
        User fakeUser = new User();
        fakeUser.setId(1L);
        when(userRepository.findByEmailId("user@test.com")).thenReturn(Optional.of(fakeUser));

        Cart fakeCart = new Cart();
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(fakeCart));

        // Force product repository to return empty (Product Not Found)
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(99L, 5);
        });

        assertEquals("Product does not exist", exception.getMessage());
    }
    @Test
    void testUpdateCartItemRemovesItem() {
        // 1. Arrange
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(10L);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5); // Current quantity is 5

        List<CartItem> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        // Stubbing repository responses
        when(userRepository.findByEmailId("user@test.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        // 2. Act: Update quantity to 0
        Cart result = cartService.updateCartItem(10L, 0);

        // 3. Assert
        assertTrue(result.getItems().isEmpty()); // The item should be completely removed from the list
        verify(cartRepository, times(1)).save(cart);
        verify(productRepository, times(1)).save(product);
    }
}
