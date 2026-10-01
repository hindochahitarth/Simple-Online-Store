package org.example.simpleonlinestore.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductReviewRequestDTO {
    private Integer rating;
    private String comment;
}
