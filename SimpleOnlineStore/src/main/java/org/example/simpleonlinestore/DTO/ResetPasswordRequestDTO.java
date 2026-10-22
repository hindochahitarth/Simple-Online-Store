package org.example.simpleonlinestore.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class ResetPasswordRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String emailId;
    @NotBlank(message = "Password is required")
    @Size(min = 8,max = 20, message = "Password must be at least 8 character and maximum 20 character")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",message = "Must contain 1 lowercase character , 1 uppercase character ,1 special symbol , 1 digit [0-9] , 1 special character and no whitespaces")

    private String newPassword;
}