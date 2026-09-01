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
import java.util.Optional;

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

    @PutMapping("/update-product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody ProductRequestDTO request){
        Product updatedProduct=productService.updateProduct(id,request);
        return ResponseEntity.ok(updatedProduct);
    }
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<HttpStatus> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/get-product-by-category/{categoryId}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable Long categoryId){
        List<Product> products=productService.getProductByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestBody Long quantity) {
        Product updatedProduct = productService.addStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }



}
