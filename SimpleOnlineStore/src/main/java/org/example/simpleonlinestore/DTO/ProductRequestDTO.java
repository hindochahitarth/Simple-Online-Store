package org.example.simpleonlinestore.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductRequestDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Description cannot be empty")
    private String description;

    @Min(value = 1,message = "Price cannot be less than or equal to 0")
    @NotBlank(message = "Price cannot be empty")
    private Long price;

    private String url; 

    private String brand;
    @Min(value = 0)
    @NotBlank(message = "Discount cannot be empty")
    private int discountPercentage;

    @Min(value = 1)
    private Long stockCount;

    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Boolean isActive;
}
