package org.example.simpleonlinestore.service.interfaces;

public interface OtpDeliveryStrategy {
    void send(String destination,String otp);
}
