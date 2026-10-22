package org.example.simpleonlinestore.DTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProductRequestDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Description cannot be empty")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.01", message = "Price cannot be less than or equal to 0")
    private BigDecimal price;

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
    private List<String> colors;
    private List<String> sizes;
}