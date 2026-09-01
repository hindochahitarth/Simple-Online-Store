package org.example.simpleonlinestore.service;

import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    //in memory data storage where otp and timestamp are stored
    private final Map<String, OtpHolder> otpStore = new HashMap<>();


    private EmailService emailService;
    private UserRepository userRepo;
    public OtpService(EmailService emailService,UserRepository userRepo){
        this.emailService=emailService;
        this.userRepo=userRepo;
    }

    public String sendOtp(String email) {
        //generates a random 6 digit number between 100000 and 999999
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        //stores otp and current time stamp
        OtpHolder otpHolder = new OtpHolder(otp, LocalDateTime.now());
        otpStore.put(email, otpHolder);
        //email service to deliver otp to email
        emailService.sendOtp(email, otp);
        //check if user exists
        boolean isExisting = userRepo.findByEmailId(email).isPresent();
        return isExisting ? "false" : "true";
    }
    // OTP expiration time limit
    private static final Duration otp_limit = Duration.ofMinutes(5);

    public boolean verifyOtp(String email, String userInputOtp) {
        // Check if an OTP exists for this email in otpStore map
        if (!otpStore.containsKey(email)) {
            return false;
        }
        //retrieves stored otp and timestamp
        OtpHolder otpHolder = otpStore.get(email);

        // 2. Check if the OTP has expired
        //Calculates elapsed time and checks if it exceeds our 5-minute constant limit.
        if (Duration.between(otpHolder.getTime(), LocalDateTime.now()).compareTo(otp_limit) > 0) {
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
