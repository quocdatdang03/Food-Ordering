package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByRestaurantId(Long restaurantId);

    @Query("SELECT f FROM Food f WHERE CONCAT(f.name, ' ', f.category.name) LIKE %?1%")
    List<Food> searchFoodByKeyword(String keyword);
}
