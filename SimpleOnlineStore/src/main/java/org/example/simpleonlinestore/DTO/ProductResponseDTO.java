package org.example.simpleonlinestore.DTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String url;
    private Long categoryId;
    private String categoryName; 
    private int discountPercentage;
    private Long stockCount;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Boolean isActive;
    private List<String> colors;
    private List<String> sizes;
}