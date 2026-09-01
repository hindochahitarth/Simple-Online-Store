package org.example.simpleonlinestore.service;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class  AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final OtpService otpService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,CartRepository cartRepository,OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository=cartRepository;
        this.otpService=otpService;

    }

    @Transactional
    public User signUp(UserRequestDTO input)  {
        if(userRepository.findByEmailId(input.getEmailId()).isPresent()){
            throw new RuntimeException("User with this email id already exist");
        }
        User user = new User();

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmailId(input.getEmailId());
        user.setRole(Roles.USER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        
        user.setActive(true);
        user.setVerified(false);
        User savedUser=userRepository.save(user);
        otpService.sendOtp(user.getEmailId());
        Cart cart=new Cart();
        cart.setUser(savedUser);
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
}


