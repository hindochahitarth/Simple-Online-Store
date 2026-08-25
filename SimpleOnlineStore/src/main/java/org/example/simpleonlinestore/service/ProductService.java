package org.example.simpleonlinestore.service;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequestDTO request) {
        log.info("Inside create product service");
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockCount(request.getStockCount());
        product.setManufacturingDate(request.getManufacturingDate());
        product.setUrl(request.getUrl());
        product.setBrand(request.getBrand());
        product.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0L);
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        product.setExpiryDate(request.getExpiryDate());

        return productRepository.save(product);

    }

    public List<Product> getAllProducts(){
            return productRepository.findAll();
    }

}

