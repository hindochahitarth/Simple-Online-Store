package org.example.simpleonlinestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wishlists")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "wishlist_id")
    private List<Product> productList=new ArrayList<>();
    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime updatedAt=LocalDateTime.now();

    public void addProduct(Product product){
        if(!productList.contains(product)){
            productList.add(product);
            updatedAt=LocalDateTime.now();
        }
    }
    public void removeProduct(Product product) {
        if (productList.remove(product)) {
            updatedAt = LocalDateTime.now();
        }
    }
    public boolean containsProduct(Product product) {
        return productList.contains(product);
    }
    public void clearProducts() {
        productList.clear();
        updatedAt = LocalDateTime.now();
    }
}