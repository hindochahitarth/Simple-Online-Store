package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {
    List<ProductReview> findByProductId(Long productId);
}
