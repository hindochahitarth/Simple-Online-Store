package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal getTotalAmountAllOrders();

}
