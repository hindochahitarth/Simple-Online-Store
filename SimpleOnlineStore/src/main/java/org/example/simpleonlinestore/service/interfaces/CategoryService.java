package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long categoryId, Category categoryDetails);
    void deleteCategory(Long categoryId);
}