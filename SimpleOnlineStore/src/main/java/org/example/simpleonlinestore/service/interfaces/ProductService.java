package org.example.service.interfaces;

import org.example.DTO.ProductRequestDTO;
import org.example.DTO.ProductResponseDTO;
import org.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO request) throws IOException;
    Page<Product> getAllProducts(Pageable pageable);
    Product updateProduct(Long id, ProductRequestDTO request);
    Optional<Product> getProductById(Long id);
    void deleteProductById(Long id);
    List<Product> getProductByCategory(String categoryName);
    Product addStock(Long id, Long quantityToAdd);
    Product updateDiscountPercentage(Long id, Integer discountPercentage);
    Page<Product> searchProducts(String keyword, Pageable pageable);
}
