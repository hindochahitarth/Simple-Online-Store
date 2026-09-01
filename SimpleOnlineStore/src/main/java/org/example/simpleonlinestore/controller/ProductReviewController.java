package org.example.simpleonlinestore.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductReviewRequestDTO;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.service.ProductReviewService;
import org.example.simpleonlinestore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.simpleonlinestore.entity.ProductReview;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/product-review")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    public ProductReviewController(ProductReviewService productReviewService){
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
