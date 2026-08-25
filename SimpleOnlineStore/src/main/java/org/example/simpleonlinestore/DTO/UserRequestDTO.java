package org.example.simpleonlinestore.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.simpleonlinestore.enums.Roles;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "Name is required ")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private  String emailId;

    @NotBlank(message = "Password is required")
    @Size(min = 8,max = 20, message = "Password must be at least 8 character and maximum 20 character")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",message = "Must contain 1 lowercase character , 1 uppercase character ,1 special symbol , 1 digit [0-9] , 1 special character and no whitespaces")

    /*
    ^ starts of string
    ?=.*[0-9] must be from 0-9 digits

    * */
    private String password;

    @NotNull(message = "Role is required")
    private Roles role;


}
