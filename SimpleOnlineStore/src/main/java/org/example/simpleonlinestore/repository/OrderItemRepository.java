package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
