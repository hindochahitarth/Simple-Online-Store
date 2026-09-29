package com.niyantras.simpleonlinestore.controller;

import lombok.extern.slf4j.Slf4j;
import com.niyantras.simpleonlinestore.DTO.ProductReviewRequestDTO;
import com.niyantras.simpleonlinestore.service.impl.ProductReviewServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.niyantras.simpleonlinestore.entity.ProductReview;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/product-review")
public class ProductReviewController {

    private final ProductReviewServiceImpl productReviewService;

    public ProductReviewController(ProductReviewServiceImpl productReviewService){
        this.productReviewService=productReviewService; 
    }


    @PostMapping("/{productId}/add-review")
    public ResponseEntity<ProductReview> createProductReview(
            @PathVariable("productId") Long productId,
            @RequestBody ProductReviewRequestDTO request) {

        ProductReview review = productReviewService.addProductReview( productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

}
