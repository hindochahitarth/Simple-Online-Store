package org.example.simpleonlinestore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false )
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(name = "imageURL")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    private Integer discountPercentage;

    @Column(name = "stocks")
    private Long stockCount;

    private LocalDate manufacturingDate;
    private LocalDate expiryDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
