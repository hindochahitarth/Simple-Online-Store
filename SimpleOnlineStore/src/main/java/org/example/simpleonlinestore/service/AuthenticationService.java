package org.example.simpleonlinestore.service;


import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.DTO.LoginUserDTO;
import org.example.simpleonlinestore.DTO.UserRequestDTO;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.enums.Roles;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class  AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,CartRepository cartRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository=cartRepository;

    }

    @Transactional
    public User signUp(UserRequestDTO input)  {
        User user = new User();

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmailId(input.getEmailId());
        user.setRole(Roles.USER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        
        user.setActive(true);

        User savedUser=userRepository.save(user);

        Cart cart=new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);


        return savedUser;
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmailId(),
                        input.getPassword()));
        return userRepository.findByEmailId(input.getEmailId()).orElseThrow();
    }
}


