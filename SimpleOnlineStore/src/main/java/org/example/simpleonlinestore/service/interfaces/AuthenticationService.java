package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.DTO.LoginUserDTO;
import org.example.simpleonlinestore.DTO.UserRequestDTO;
import org.example.simpleonlinestore.entity.User;

public interface AuthenticationService {
    User signUp(UserRequestDTO input);
    User verifyOtp(String email, String otp);
    User authenticate(LoginUserDTO input);
    void resendOtp(String email);
    void logout();
    void resetPassword(String email, String newPassword);
}