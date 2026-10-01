package org.example.service.interfaces;

import org.example.DTO.ProductReviewRequestDTO;
import org.example.entity.ProductReview;

public interface ProductReviewService {
    ProductReview addProductReview(Long productId, ProductReviewRequestDTO request);
}
