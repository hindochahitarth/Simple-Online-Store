package com.niyantras.simpleonlinestore.service.interfaces;

public interface OtpService {
    void sendOtp(String email);
    boolean verifyOtp(String email, String userInputOtp);
}
