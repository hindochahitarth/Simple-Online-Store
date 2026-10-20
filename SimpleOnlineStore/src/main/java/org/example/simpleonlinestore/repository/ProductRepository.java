package org.example.simpleonlinestore.repository;

import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryName(String categoryName);
    Page<Product> findByCategoryId(Long id,Pageable pageable);

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

    @Query("SELECT p FROM Product p WHERE p.createdAt >= :cutOffDate ORDER BY p.createdAt DESC")
    Page<Product> findRecentArrivals(@Param("cutOffDate") LocalDateTime cutOffDate, Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Product p JOIN p.sizes c WHERE p.category.id = :categoryId AND LOWER(c) IN :sizes",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Product p JOIN p.sizes c WHERE p.category.id = :categoryId AND LOWER(c) IN :sizes")
    Page<Product> searchByCategoryAndSizes(
            @Param("categoryId") Long categoryId,
            @Param("sizes") List<String> sizes,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Product p JOIN p.sizes s JOIN p.colors c WHERE p.category.id = :categoryId AND LOWER(s) IN :sizes AND LOWER(c) IN :colors")
    Page<Product> searchByCategoryAndSizesAndColors(
            @Param("categoryId") Long categoryId,
            @Param("sizes") List<String> sizes,
            @Param("colors") List<String> colors,
            Pageable pageable
    );
    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.colors c " +
            "LEFT JOIN p.sizes s " +
            "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:colors IS NULL OR c IN :colors) " +
            "AND (:sizes IS NULL OR s IN :sizes) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:latestArrivals IS NULL OR :latestArrivals = FALSE OR p.createdAt >= :cutoffDate)")
    Page<Product> findByDynamicFilters(
            @Param("categoryId") Long categoryId,
            @Param("colors") List<String> colors,
            @Param("sizes") List<String> sizes,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("latestArrivals") Boolean latestArrivals,
            @Param("cutoffDate") LocalDateTime cutoffDate,
            Pageable pageable
    );
}