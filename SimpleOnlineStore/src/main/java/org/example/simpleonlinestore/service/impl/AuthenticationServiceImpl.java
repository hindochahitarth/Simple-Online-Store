package org.example.simpleonlinestore.service.impl;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.LoginUserDTO;
import org.example.simpleonlinestore.DTO.UserRequestDTO;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.enums.Roles;
import org.example.simpleonlinestore.exception.UserAlreadyExistsException;
import org.example.simpleonlinestore.repository.CartRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class  AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final OtpServiceImpl otpService;
    private final UserFactory userFactory;
    private final  CartFactory cartFactory;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, CartRepository cartRepository, OtpServiceImpl otpService, UserFactory userFactory, CartFactory cartFactory) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository=cartRepository;
        this.otpService=otpService;

        this.userFactory = userFactory;
        this.cartFactory = cartFactory;
    }

    @Transactional
    public User signUp(UserRequestDTO input)  {
        if(userRepository.findByEmailId(input.getEmailId()).isPresent()){
            throw new UserAlreadyExistsException("User with this email id already exist");
        }
//        User user = User.builder()
//                        .firstName(input.getFirstName())
//                        .lastName(input.getLastName())
//                        .emailId(input.getEmailId())
//                        .role(Roles.USER)
//                        .password(passwordEncoder.encode(input.getPassword()))
//                        .phoneNumber(input.getPhoneNumber())
//                        .isActive(true)
//                        .verified(false)
//                        .build();
//        user.setFirstName(input.getFirstName());
//        user.setLastName(input.getLastName());
//        user.setEmailId(input.getEmailId());
//        user.setRole(Roles.USER);
//        user.setPassword(passwordEncoder.encode(input.getPassword()));
//        user.setPhoneNumber(input.getPhoneNumber());
//        user.setActive(true);
//        user.setVerified(false);
        User user=userFactory.createUser(input);
        User savedUser=userRepository.save(user);
        otpService.sendOtp(user.getEmailId());
//        Cart cart=Cart.builder()
//                        .user(savedUser)
//                                .build();
        //cart.setUser(savedUser);
        Cart cart=cartFactory.createCartForUser(savedUser);
        cartRepository.save(cart);


        return savedUser;
    }
    public User verifyOtp(String email,String otp){
        boolean valid=otpService.verifyOtp(email,otp);

        if(!valid){
            throw new RuntimeException("Invalid or Expired OTP");
        }
        User user=userRepository.findByEmailId(email).orElseThrow(() -> new RuntimeException("User does not exist"));
        log.info("Inside auth service value of valid is "+valid);

        log.info("Before "+String.valueOf(user.isVerified()));
        user.setVerified(true);
        log.info("After "+String.valueOf(user.isVerified()));
        return userRepository.save(user);
    }
    public User authenticate(LoginUserDTO input) {
        User user=userRepository.findByEmailId(input.getEmailId()).orElseThrow(() -> new RuntimeException("User does not exist"));
        if(!user.isVerified()){
            throw new RuntimeException("PLease verify your email before login");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmailId(),
                        input.getPassword()));
        return userRepository.findByEmailId(input.getEmailId()).orElseThrow();
    }

    @Override
    public void resendOtp(String email) {
        User user=userRepository.findByEmailId(email).orElseThrow(() -> new RuntimeException("User does not exist"));
        if(user.isVerified()){
            throw new RuntimeException("This User Account is already Verified.Please proceed to Login");
        }
        otpService.sendOtp(email);
        

    }
    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
    @Override
    public void resetPassword(String email, String newPassword) {
        // Fetch the user profile
        User user = userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        // Encrypt and save the new password
        user.setPassword(passwordEncoder.encode(newPassword));

        if (!user.isVerified()) {
            user.setVerified(true);
        }

        userRepository.save(user);
        log.info("Password successfully reset for user: {}", email);

    }
}


