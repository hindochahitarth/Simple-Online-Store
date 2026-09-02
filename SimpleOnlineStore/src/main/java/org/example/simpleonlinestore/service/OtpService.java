package org.example.simpleonlinestore.service;

import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;

@Service
public class OtpService {

    //in memory data storage where otp and timestamp are stored
    private final Map<String, OtpHolder> otpStore = new HashMap<>();


    private final EmailService emailService;
    private final UserRepository userRepo;
    public OtpService(EmailService emailService,UserRepository userRepo){
        this.emailService=emailService;
        this.userRepo=userRepo;
    }

    public void sendOtp(String email) {
        SecureRandom random = new SecureRandom();

        String otp = String.valueOf(random.nextInt(100000,1000000));
        //stores otp and current time stamp
        OtpHolder otpHolder = new OtpHolder(otp, LocalDateTime.now());
        otpStore.put(email, otpHolder);
        //email service to deliver otp to email
        emailService.sendOtp(email, otp);
        //check if user exists
        userRepo.findByEmailId(email);

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
        if (Duration.between(otpHolder.getTime(), LocalDateTime.now()).compareTo(otp_limit) > 0) {
        //returns positive value if elapsed time is greater than limit(expired)
            //returns 0 if they are equal
            //< 0 if otp is valid 
            otpStore.remove(email); // Clean up expired OTP
            return false;
        }

        // 3. Match the user input with the stored OTP
        if (otpHolder.getOtp().equals(userInputOtp)) {
            otpStore.remove(email); // Clear OTP so it cannot be reused
            return true;
        }
//if entered wrong otp then return false.
        return false;
    }
    private static class OtpHolder {
        private final String otp;
        private final LocalDateTime time;

        public OtpHolder(String otp, LocalDateTime time) {
            this.otp = otp;
            this.time = time;
        }

        public String getOtp() { return otp; }
        public LocalDateTime getTime() { return time; }
    }
}
