package org.example.simpleonlinestore.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductRequestDTO {
    private String name;
    private String description;
    private Long price;
    private String url;
    private String brand;
    private Long discount;
    private Long stockCount;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Boolean isActive;
}
