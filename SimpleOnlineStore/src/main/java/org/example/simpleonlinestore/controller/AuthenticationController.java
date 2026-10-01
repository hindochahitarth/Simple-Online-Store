package org.example.controller;

import jakarta.validation.Valid;
import org.example.DTO.LoginResponseDTO;
import org.example.DTO.LoginUserDTO;
import org.example.DTO.ResetPasswordRequestDTO;
import org.example.DTO.UserRequestDTO;
import org.example.config.JwtService;
import org.example.entity.User;
import org.example.service.impl.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationServiceImpl authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationServiceImpl authenticationService) {
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
    @PostMapping("/resendOtp")
    public ResponseEntity<Map<String,String>> resendOtp(@RequestParam String email){
        authenticationService.resendOtp(email);
        return ResponseEntity.ok(
                Map.of(
                        "status","Success",
                        "message","New OTP Resend successfully"
                )
        );
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        authenticationService.resetPassword(
                request.getEmailId(),
                request.getNewPassword()
        );
        return ResponseEntity.ok("Password reset successfully. You can now login with your new password.");
    }
}

