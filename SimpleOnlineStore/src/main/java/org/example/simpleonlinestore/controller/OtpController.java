package org.example.simpleonlinestore.controller;

import org.example.simpleonlinestore.service.OtpService;
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
}
