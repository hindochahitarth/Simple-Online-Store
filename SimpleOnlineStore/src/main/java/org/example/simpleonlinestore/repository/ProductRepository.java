package org.example.simpleonlinestore.repository;

import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryName(String categoryName);
    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
    Page<Product> findAll(Pageable pageable);
    boolean existsByName(String name);
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stockCount = p.stockCount - :quantity WHERE p.id = :productId AND p.stockCount >= :quantity")
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);


}
