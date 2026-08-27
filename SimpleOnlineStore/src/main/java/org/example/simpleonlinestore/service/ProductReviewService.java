package org.example.simpleonlinestore.service;

import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductReviewRequestDTO;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.ProductReview;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.ProductReviewRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductReviewService {
    private ProductRepository productRepository;
    private ProductReviewRepository productReviewRepository;
    private UserRepository userRepository;

    public ProductReviewService(ProductReviewRepository productReviewRepository,UserRepository userRepository,ProductRepository productRepository){
        this.productReviewRepository=productReviewRepository;
        this.userRepository=userRepository;
        this.productRepository=productRepository;
    }

    public ProductReview addProductReview(Long userId, Long productId, ProductReviewRequestDTO request){
        if(request.getRating()<1 || request.getRating()>5){
            throw new RuntimeException("Rating must be Between 1 - 5");
        }
        Product product=productRepository.findById(productId).orElseThrow(() ->new RuntimeException("Product with id "+productId+" does not exist"));
        User user=userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id "+userId+"does not exist"));


        ProductReview productReview=new ProductReview();

        productReview.setComment(request.getComment());
        productReview.setProduct(product);
        productReview.setUser(user);
        productReview.setRating(request.getRating());

        return productReviewRepository.save(productReview);
    }
}
