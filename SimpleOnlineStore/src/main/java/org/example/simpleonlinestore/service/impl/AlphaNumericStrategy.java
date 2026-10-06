package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.service.interfaces.OtpGenerationStrategy;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component("alphaNumeric")
public class AlphaNumericStrategy implements OtpGenerationStrategy {
    private static final String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    private final SecureRandom random=new SecureRandom();

    @Override
    public String generate() {
        StringBuilder sb=new StringBuilder(6);
        for(int i=0;i<6;i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
