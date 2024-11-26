package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
