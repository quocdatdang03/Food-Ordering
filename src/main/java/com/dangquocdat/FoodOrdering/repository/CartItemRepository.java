package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
