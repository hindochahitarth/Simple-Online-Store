package org.example.simpleonlinestore.service;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Category;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.repository.CategoryRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
    }

    public Product createProduct(ProductRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));

        log.info("Inside create product service");
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockCount(request.getStockCount());
        product.setManufacturingDate(request.getManufacturingDate());
        product.setUrl(request.getUrl());
        product.setCategory(category);
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        product.setExpiryDate(request.getExpiryDate());

        return productRepository.save(product);

    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public Product updateProduct(Long id,ProductRequestDTO request){
        Product product=productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id "+id+" not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setUrl(request.getUrl());
        product.setStockCount(request.getStockCount());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return productRepository.save(product);

    }
    public Optional<Product> getProductById(Long id){
        Product product=productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id "+id+" not found"));

        return productRepository.findById(id);
    }

    public void deleteProductById(Long id){
        Product product=productRepository.findById(id) .orElseThrow(() -> new RuntimeException("Product with id "+id+" not found"));
        productRepository.deleteById(id);
    }
    public List<Product> getProductByCategory(Long categoryId){
        return productRepository.findByCategoryId(categoryId);
    }
    public Product addStock(Long id, Long quantityToAdd) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));

        product.setStockCount(product.getStockCount() + quantityToAdd);
        return productRepository.save(product);
    }
    public Product updateDiscountPercentage(Long id, Integer discountPercentage) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));

        if (discountPercentage!=null && (discountPercentage<0 || discountPercentage>100)) {
            throw new RuntimeException("Discount percentage must be between 0 and 100");
        }

        product.setDiscountPercentage(discountPercentage);
        return productRepository.save(product);
    }
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }



}

