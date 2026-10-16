package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Product;
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
    Page<ProductResponseDTO> getProductsByPriceRange(Long minPrice, Long maxPrice, Pageable pageable);
    Page<Product> getProductsByName(Pageable pageable);
    Page<Product> getProductsByCategoryAndColors(Long categoryId, List<String> colors, Pageable pageable);
    Page<Product> getDiscountedProducts(Pageable pageable);
    Page<Product> getRecentArrivalsProducts(Pageable pageable);
    Page<Product> getProductsByCategoryAndSizes(Long categoryId, List<String> sizes, Pageable pageable);
    Page<Product> getProductsByCategoryAndSizesAndColors(Long categoryId, List<String> colors,List<String> sizes, Pageable pageable);

}