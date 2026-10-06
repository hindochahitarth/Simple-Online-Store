package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.service.interfaces.EmailService;
import org.example.simpleonlinestore.service.interfaces.OtpDeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class EmailDeliveryStrategy implements OtpDeliveryStrategy {
    private final EmailService emailService;

    public EmailDeliveryStrategy(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(String email, String otp) {
            emailService.sendOtp(email,otp);
    }
}
