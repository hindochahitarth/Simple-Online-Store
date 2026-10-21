package org.example.simpleonlinestore.service.impl.auth;
import org.example.simpleonlinestore.entity.OtpEntity;
import org.example.simpleonlinestore.repository.OtpRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.OtpDeliveryStrategy;
import org.example.simpleonlinestore.service.interfaces.OtpGenerationStrategy;
import org.example.simpleonlinestore.service.interfaces.OtpService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    //in memory data storage where otp and timestamp are stored
    private final Map<String, OtpHolder> otpStore = new HashMap<>();
    private final OtpGenerationStrategy otpGenerationStrategy;
    private final OtpDeliveryStrategy otpDeliveryStrategy;
    private final OtpRepository otpRepository;
    public OtpServiceImpl(@Qualifier("alphaNumeric") OtpGenerationStrategy otpGenerationStrategy, @Qualifier("emailDelivery") OtpDeliveryStrategy otpDeliveryStrategy, OtpRepository otpRepository){
        this.otpGenerationStrategy=otpGenerationStrategy;
        this.otpDeliveryStrategy=otpDeliveryStrategy;
        this.otpRepository = otpRepository;
    }

    public void sendOtp(String target) {
            String otp=otpGenerationStrategy.generate();
            LocalDateTime expiryTime=LocalDateTime.now().plusMinutes(2);
            OtpEntity otpEntity=new OtpEntity(target,otp,expiryTime);
            otpRepository.save(otpEntity);
            otpDeliveryStrategy.send(target,otp);
    }
    // OTP expiration time limit
    private static final Duration otp_limit = Duration.ofMinutes(2);
    public boolean verifyOtp(String email, String userInputOtp) {
        Optional<OtpEntity> otpOptional=otpRepository.findById(email);
        // Check if an OTP exists for this email in otpStore map
        if (otpOptional.isEmpty()) {
            return false;
        }
        //retrieves stored otp and timestamp
        OtpEntity otpEntity=otpOptional.get();

        // 2. Check if the OTP has expired
        //Calculates  time .
        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
        //returns positive value if elapsed time is greater than limit(expired)
            //returns 0 if they are equal
            //< 0 if otp is valid 
            otpRepository.delete(otpEntity); // Clean up expired OTP
            return false;
        }
        // 3. Match the user input with the stored OTP
        if (otpEntity.getOtp().equals(userInputOtp)) {
            otpRepository.delete(otpEntity); // Clear OTP so it cannot be reused
            return true;
        }
        //if entered wrong otp then return false.
        return false;
    }
    private record OtpHolder(String otp,LocalDateTime time){}
}
