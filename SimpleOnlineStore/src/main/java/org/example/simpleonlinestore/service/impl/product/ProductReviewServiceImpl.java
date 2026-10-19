package org.example.simpleonlinestore.service.impl.product;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductReviewRequestDTO;
import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.ProductReview;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.OrderRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.ProductReviewRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.ProductReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository, UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository){
        this.productReviewRepository=productReviewRepository;
        this.userRepository=userRepository;
        this.productRepository=productRepository;
        this.orderRepository = orderRepository;
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
        log.info("user id"+user.getId());
        List<Order> orders= orderRepository.findByUserId(user.getId());
        log.info("order"+ orders);
        boolean hasOrderedProduct = orders.stream()
                .flatMap(order -> order.getItems().stream()) // Assumes Order has a list of OrderItems
                .anyMatch(item -> item.getProduct().getId().equals(productId));

        if (!hasOrderedProduct) {
            throw new RuntimeException("You can only review products you have officially purchased.");
        }
        ProductReview productReview=ProductReview
                .builder()
                        .comment(request.getComment())
                        .product(product)
                        .user(user)
                        .rating(request.getRating())
                        .build();
        return productReviewRepository.save(productReview);
    }
}