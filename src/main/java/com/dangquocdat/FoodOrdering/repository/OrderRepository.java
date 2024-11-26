package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findByCustomerId(Long customerId);

    Optional<List<Order>> findByRestaurantId(Long restaurantId);
}
