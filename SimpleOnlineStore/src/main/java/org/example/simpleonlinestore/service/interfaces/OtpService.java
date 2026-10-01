package org.example.service.interfaces;

public interface OtpService {
    void sendOtp(String email);
    boolean verifyOtp(String email, String userInputOtp);
}
