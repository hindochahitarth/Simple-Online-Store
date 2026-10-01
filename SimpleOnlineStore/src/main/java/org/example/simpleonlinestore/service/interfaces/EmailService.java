package org.example.simpleonlinestore.service.interfaces;

public interface EmailService {
    void sendOtp(String toEmail, String otp);
    void sendNotification(String toEmail,String mssg);
}
