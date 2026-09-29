package com.niyantras.simpleonlinestore.service.interfaces;

import com.niyantras.simpleonlinestore.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long categoryId, Category categoryDetails);
    void deleteCategory(Long categoryId);
}
