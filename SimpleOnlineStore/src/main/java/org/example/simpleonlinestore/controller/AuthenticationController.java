package org.example.simpleonlinestore.controller;

import jakarta.validation.Valid;
import org.example.simpleonlinestore.DTO.LoginResponseDTO;
import org.example.simpleonlinestore.DTO.LoginUserDTO;
import org.example.simpleonlinestore.DTO.UserRequestDTO;
import org.example.simpleonlinestore.config.JwtService;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> register(@RequestBody @Valid UserRequestDTO registerUserDto) {
        authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(
                Map.of(
                        "status","Success",
                        "message","OTP send to your email"
                )
        );
    }
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        authenticationService.verifyOtp(email, otp);
        return ResponseEntity.ok(
                Map.of(
                        "status","Success",
                        "message","Email verified successfully"
                )
        );
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponseDTO loginResponse = new LoginResponseDTO().setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}

