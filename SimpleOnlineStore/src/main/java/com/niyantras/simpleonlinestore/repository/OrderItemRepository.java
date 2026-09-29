package com.niyantras.simpleonlinestore.repository;

import com.niyantras.simpleonlinestore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
