package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryId(Long categoryId);
}
