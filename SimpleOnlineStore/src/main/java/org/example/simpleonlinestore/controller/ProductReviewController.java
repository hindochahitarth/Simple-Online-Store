package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.ProductReviewRequestDTO;
import org.example.service.impl.ProductReviewServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.entity.ProductReview;
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
