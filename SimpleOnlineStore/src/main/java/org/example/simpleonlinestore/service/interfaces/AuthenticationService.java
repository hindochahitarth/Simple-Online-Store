package org.example.service.interfaces;

import org.example.DTO.LoginUserDTO;
import org.example.DTO.UserRequestDTO;
import org.example.entity.User;

public interface AuthenticationService {
    User signUp(UserRequestDTO input);
    User verifyOtp(String email, String otp);
    User authenticate(LoginUserDTO input);
    void resendOtp(String email);
    void logout();
    void resetPassword(String email, String newPassword);

}
