package org.example.simpleonlinestore.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping(value = "/create-product", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@ModelAttribute ProductRequestDTO request) throws IOException {
        log.info("Inside Create product controller ");
        Product savedProduct=productService.createProduct(request);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/get-all-products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.getAllProducts(pageable)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody ProductRequestDTO request){
        Product updatedProduct=productService.updateProduct(id,request);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<HttpStatus> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/get-product-by-category/{categoryName}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String categoryName){
        List<Product> products=productService.getProductByCategory(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestBody Long quantity) {
        Product updatedProduct = productService.addStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/discount")
    public ResponseEntity<Product> updateDiscountPercentage(
            @PathVariable Long id,
            @RequestParam Integer discountPercentage) {

        Product updatedProduct = productService.updateDiscountPercentage(id, discountPercentage);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.searchProducts(keyword, pageable)
        );
    }
}
