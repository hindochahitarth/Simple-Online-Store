package org.example.simpleonlinestore.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Category;
import org.example.simpleonlinestore.entity.Image;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.mapper.ProductMapper;
import org.example.simpleonlinestore.repository.CategoryRepository;
import org.example.simpleonlinestore.repository.ImageRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, ProductMapper productMapper) {

        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
        this.imageRepository=imageRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO request) throws IOException {
        if(productRepository.existsByName(request.getName())){
            throw new RuntimeException("Product Already exists");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));
        MultipartFile file = request.getFile();
        Image image= Image.builder()
                .name(file.getOriginalFilename()) //'file.getOriginalFilename()' extracts the original name of the uploaded file
                .type(file.getContentType()) //'file.getContentType()' extracts the MIME type (jpg or png)
                .imageData(file.getBytes()) //'file.getBytes()' reads the raw binary payload of the image directly from memory into a byte array (byte[]).
                .build();

        log.info("Inside create product service");
        Product product = productMapper.toEntity(request);
        product.setImage(image);
        product.setCategory(category);
        product.setImageUrl(file.getOriginalFilename());
        Product savedProduct= productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    public Page<Product> getAllProducts(Pageable pageable) {

        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        return productRepository.findAll(pageable);
    }
    public Product updateProduct(Long id,ProductRequestDTO request){
        Product product=productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id "+id+" not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        //product.setUrl(request.getUrl());
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
    public List<Product> getProductByCategory(String categoryName){
        return productRepository.findByCategoryName(categoryName);
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
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
}

