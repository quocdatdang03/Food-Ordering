package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
