package com.niyantras.simpleonlinestore.service.interfaces;

import com.niyantras.simpleonlinestore.DTO.LoginUserDTO;
import com.niyantras.simpleonlinestore.DTO.UserRequestDTO;
import com.niyantras.simpleonlinestore.entity.User;

public interface AuthenticationService {
    User signUp(UserRequestDTO input);
    User verifyOtp(String email, String otp);
    User authenticate(LoginUserDTO input);
    void resendOtp(String email);
    void logout();
    void resetPassword(String email, String newPassword);

}
