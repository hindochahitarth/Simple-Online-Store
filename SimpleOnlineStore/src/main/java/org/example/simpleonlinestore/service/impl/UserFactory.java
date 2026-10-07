package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.DTO.UserRequestDTO;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.enums.Roles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserRequestDTO input) {
        return User.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .emailId(input.getEmailId())
                .role(Roles.USER) 
                .password(passwordEncoder.encode(input.getPassword()))
                .phoneNumber(input.getPhoneNumber())
                .isActive(true)
                .verified(false)
                .build();
    }
}
