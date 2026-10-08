package org.example.simpleonlinestore.service.impl.auth;

import org.example.simpleonlinestore.service.interfaces.OtpDeliveryStrategy;
import org.springframework.stereotype.Component;

@Component("smsDelivery")
public class SmsDeliveryStrategy implements OtpDeliveryStrategy {

    @Override
    public void send(String destination, String otp) {

    }
}
