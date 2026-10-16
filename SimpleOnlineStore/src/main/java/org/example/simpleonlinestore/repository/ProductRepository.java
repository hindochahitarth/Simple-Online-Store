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
import java.util.Optional;

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

    @Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") Long minPrice, @Param("maxPrice") Long maxPrice, Pageable pageable);

    @Query("SELECT MIN(p.price) FROM Product p")
    Long getMinPrice();

    @Query("SELECT MAX(p.price) FROM Product p")
    Long getMaxPrice();
    @Query(value = "SELECT DISTINCT p FROM Product p JOIN p.colors c WHERE p.category.id = :categoryId AND LOWER(c) IN :colors",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Product p JOIN p.colors c WHERE p.category.id = :categoryId AND LOWER(c) IN :colors")
    Page<Product> searchByCategoryAndColors(
            @Param("categoryId") Long categoryId,
            @Param("colors") List<String> colors,
            Pageable pageable);

    @Query("SELECT p from Product p WHERE p.discountPercentage > 0")
    Page<Product> findAllDiscountedProducts(Pageable pageable);
}
