package org.example.simpleonlinestore.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping("/create-product")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO request){
        log.info("Inside Create product controller ");
        Product savedProduct=productService.createProduct(request);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
    @GetMapping("/get-all-products")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product>  products=productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

}
