package org.example.simpleonlinestore.service.impl.auth;
import org.example.simpleonlinestore.service.interfaces.OtpGenerationStrategy;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component("sixDigit")
public class SixDigitNumericStrategy implements OtpGenerationStrategy {

    private final SecureRandom random=new SecureRandom();
    @Override
    public String generate() {
        return String.valueOf(random.nextInt(100000,1000000));
    }
}
