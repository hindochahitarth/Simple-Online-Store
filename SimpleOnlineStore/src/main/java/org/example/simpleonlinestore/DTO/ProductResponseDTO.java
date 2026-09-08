package org.example.simpleonlinestore.DTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String url;
    private Long categoryId;
    private String categoryName; 
    private int discountPercentage;
    private Long stockCount;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Boolean isActive;
}
