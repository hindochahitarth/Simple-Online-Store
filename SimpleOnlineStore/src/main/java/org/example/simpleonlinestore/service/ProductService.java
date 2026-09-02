package org.example.simpleonlinestore.service;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Category;
import org.example.simpleonlinestore.entity.Image;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.repository.CategoryRepository;
import org.example.simpleonlinestore.repository.ImageRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository,ImageRepository imageRepository) {

        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
        this.imageRepository=imageRepository;
    }

    public Product createProduct(ProductRequestDTO request) throws IOException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));
        MultipartFile file = request.getFile();
        Image image= Image.builder()
                .name(file.getOriginalFilename()) //'file.getOriginalFilename()' extracts the original name of the uploaded file
                .type(file.getContentType()) //'file.getContentType()' extracts the MIME type (jpg or png)
                .imageData(file.getBytes()) //'file.getBytes()' reads the raw binary payload of the image directly from memory into a byte array (byte[]).
                .build();

        log.info("Inside create product service");
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockCount(request.getStockCount());
        product.setManufacturingDate(request.getManufacturingDate());
        //product.setUrl(request.getUrl());
        product.setImage(image);
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
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    public String uploadImage(MultipartFile file) throws IOException {
        Image image= Image.builder()
                .name(file.getOriginalFilename()) //'file.getOriginalFilename()' extracts the original name of the uploaded file
                .type(file.getContentType()) //'file.getContentType()' extracts the MIME type (jpg or png)
                .imageData(file.getBytes()) //'file.getBytes()' reads the raw binary payload of the image directly from memory into a byte array (byte[]).
                .build();
        imageRepository.save(image);
        return file.getOriginalFilename()+"Uploaded ";
    }



}

