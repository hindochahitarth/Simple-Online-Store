package com.niyantras.simpleonlinestore.service.interfaces;

import com.niyantras.simpleonlinestore.DTO.ProductReviewRequestDTO;
import com.niyantras.simpleonlinestore.entity.ProductReview;

public interface ProductReviewService {
    ProductReview addProductReview(Long productId, ProductReviewRequestDTO request);
}
