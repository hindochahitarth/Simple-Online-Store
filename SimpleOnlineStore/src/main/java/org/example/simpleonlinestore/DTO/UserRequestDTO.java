package org.example.simpleonlinestore.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.simpleonlinestore.enums.Roles;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private  String emailId;

    @NotBlank(message = "Password is required")
    @Size(min = 8,max = 20, message = "Password must be at least 8 character and maximum 20 character")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",message = "Must contain 1 lowercase character , 1 uppercase character ,1 special symbol , 1 digit [0-9] , 1 special character and no whitespaces")

    /*
    ^ starts of string
    ?=.*[0-9] must be from 0-9 digits

    * */
    private String password;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,10}$", message = "Phone number must be a valid ")
    private String phoneNumber;
    
}
