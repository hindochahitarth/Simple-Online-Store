package org.example.simpleonlinestore.service.impl.cart;

import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CartFactory {

    public Cart createCartForUser(User user) {
        return Cart.builder()
                .user(user) 
                .build();
    }
}

