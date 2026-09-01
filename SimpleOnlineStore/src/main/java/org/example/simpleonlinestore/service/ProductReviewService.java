package org.example.simpleonlinestore.service;

import org.example.simpleonlinestore.DTO.ProductReviewRequestDTO;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.ProductReview;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.ProductReviewRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductReviewService {
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final UserRepository userRepository;

    public ProductReviewService(ProductReviewRepository productReviewRepository,UserRepository userRepository,ProductRepository productRepository){
        this.productReviewRepository=productReviewRepository;
        this.userRepository=userRepository;
        this.productRepository=productRepository;
    }
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
    public ProductReview addProductReview(Long productId, ProductReviewRequestDTO request){
        if(request.getRating()<1 || request.getRating()>5){
            throw new RuntimeException("Rating must be Between 1 - 5");
        }
        Product product=productRepository.findById(productId).orElseThrow(() ->new RuntimeException("Product with id "+productId+" does not exist"));
        User user=getLoggedInUser();

        ProductReview productReview=new ProductReview();

        productReview.setComment(request.getComment());
        productReview.setProduct(product);
        productReview.setUser(user);
        productReview.setRating(request.getRating());

        return productReviewRepository.save(productReview);
    }
}
