package org.example.simpleonlinestore.controller;

import org.example.simpleonlinestore.entity.Category;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/get-all-categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories=categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-category/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDetails));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
