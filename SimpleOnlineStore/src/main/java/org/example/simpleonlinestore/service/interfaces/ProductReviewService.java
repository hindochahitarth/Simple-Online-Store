package org.example.simpleonlinestore.service.interfaces;

import org.example.simpleonlinestore.DTO.ProductReviewRequestDTO;
import org.example.simpleonlinestore.entity.ProductReview;

public interface ProductReviewService {
    ProductReview addProductReview(Long productId, ProductReviewRequestDTO request);
}
