package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
