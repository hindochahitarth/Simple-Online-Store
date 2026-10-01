package org.example.DTO;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String emailId;
    private String newPassword;
}

