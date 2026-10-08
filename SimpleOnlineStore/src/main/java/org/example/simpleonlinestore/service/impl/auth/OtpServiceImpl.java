package org.example.simpleonlinestore.service.impl.auth;

import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.OtpDeliveryStrategy;
import org.example.simpleonlinestore.service.interfaces.OtpGenerationStrategy;
import org.example.simpleonlinestore.service.interfaces.OtpService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpServiceImpl implements OtpService {

    //in memory data storage where otp and timestamp are stored
    private final Map<String, OtpHolder> otpStore = new HashMap<>();


    private final EmailServiceImpl emailService;
    private final UserRepository userRepo;
    private final OtpGenerationStrategy otpGenerationStrategy;
    private final OtpDeliveryStrategy otpDeliveryStrategy;
    public OtpServiceImpl(EmailServiceImpl emailService, UserRepository userRepo, @Qualifier("alphaNumeric") OtpGenerationStrategy otpGenerationStrategy, OtpDeliveryStrategy otpDeliveryStrategy){
        this.emailService=emailService;
        this.userRepo=userRepo;
        this.otpGenerationStrategy=otpGenerationStrategy;
        this.otpDeliveryStrategy=otpDeliveryStrategy;
    }

    public void sendOtp(String target) {
            String otp=otpGenerationStrategy.generate();

            OtpHolder otpHolder=new OtpHolder(otp,LocalDateTime.now());
            otpStore.put(target,otpHolder);

            otpDeliveryStrategy.send(target,otp);
    }
    // OTP expiration time limit
    private static final Duration otp_limit = Duration.ofMinutes(2);

    public boolean verifyOtp(String email, String userInputOtp) {
        // Check if an OTP exists for this email in otpStore map
        if (!otpStore.containsKey(email)) {
            return false;
        }
        //retrieves stored otp and timestamp
        OtpHolder otpHolder = otpStore.get(email);

        // 2. Check if the OTP has expired
        //Calculates  time .
        if (Duration.between(otpHolder.time(), LocalDateTime.now()).compareTo(otp_limit) > 0) {
        //returns positive value if elapsed time is greater than limit(expired)
            //returns 0 if they are equal
            //< 0 if otp is valid 
            otpStore.remove(email); // Clean up expired OTP
            return false;
        }

        // 3. Match the user input with the stored OTP
        if (otpHolder.otp().equals(userInputOtp)) {
            otpStore.remove(email); // Clear OTP so it cannot be reused
            return true;
        }
//if entered wrong otp then return false.
        return false;
    }
    private record OtpHolder(String otp,LocalDateTime time){}

}
