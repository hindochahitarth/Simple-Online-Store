package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryName(String categoryName);
    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}
