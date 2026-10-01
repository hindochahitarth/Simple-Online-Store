package org.example.simpleonlinestore.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    @NotBlank(message = "Street address is required")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Postal code is required")
    @Size(min = 3, max = 10, message = "Postal code must be between 3 and 10 characters")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Address type is required")
    @Pattern(regexp = "^(HOME|WORK|SHIPPING|BILLING)$", message = "Address type must be HOME, WORK, SHIPPING, or BILLING")
    private String addressType;

    private boolean isDefault = false;
}
