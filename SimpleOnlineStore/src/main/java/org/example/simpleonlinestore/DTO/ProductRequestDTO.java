package org.example.simpleonlinestore.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.example.simpleonlinestore.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class ProductRequestDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Description cannot be empty")
    private String description;

    @Min(value = 1,message = "Price cannot be less than or equal to 0")
    @NotNull(message = "Price cannot be empty")
    private Long price;

    private String url; 

    private Long categoryId;
    @Min(value = 0, message = "Discount cannot be a negative number.")
    @NotNull(message = "Discount cannot be empty")
    private Integer discountPercentage;

    @NotNull(message = "Stock count is required.")
    @Min(value = 1, message = "Stock count cannot be a zero negative number.")

    private Long stockCount;

    @NotNull(message = "Manufacturing Date is required")
    @PastOrPresent(message = "Manufacturing Date cannot be in future")
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Boolean isActive;

    private MultipartFile file;
}
