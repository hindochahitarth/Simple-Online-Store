package org.example.simpleonlinestore.controller;

import org.example.simpleonlinestore.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpController {

    private OtpService otpService;

    public OtpController(OtpService otpService){
        this.otpService=otpService;
    }
    @PostMapping("/send")
    public Map<String, String> sendOtp(@RequestParam String email) {
        String isNewUser = otpService.sendOtp(email);
        return Map.of("status", "OTP sent", "isNewUser", isNewUser);
    }
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);

        if (isValid) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "OTP verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid or expired OTP"));
        }
    }
}
