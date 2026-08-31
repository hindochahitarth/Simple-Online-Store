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

    private Map<String, OtpHolder> otpStore = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; //  for hashing passwords

    public String sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        OtpHolder otpHolder = new OtpHolder(otp, LocalDateTime.now());
        otpStore.put(email, otpHolder);

        emailService.sendOtp(email, otp);

        boolean isExisting = userRepo.findByEmailId(email).isPresent();
        return isExisting ? "false" : "true";
    }
    // OTP expiration time limit
    private static final Duration OTP_EXPIRATION_LIMIT = Duration.ofMinutes(5);

    public boolean verifyOtp(String email, String userInputOtp) {
        // Check if an OTP exists for this email
        if (!otpStore.containsKey(email)) {
            return false;
        }

        OtpHolder otpHolder = otpStore.get(email);

        // 2. Check if the OTP has expired
        if (Duration.between(otpHolder.getTime(), LocalDateTime.now()).compareTo(OTP_EXPIRATION_LIMIT) > 0) {
            otpStore.remove(email); // Clean up expired OTP
            return false;
        }

        // 3. Match the user input with the stored OTP
        if (otpHolder.getOtp().equals(userInputOtp)) {
            otpStore.remove(email); // Clear OTP so it cannot be reused 
            return true;
        }

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
