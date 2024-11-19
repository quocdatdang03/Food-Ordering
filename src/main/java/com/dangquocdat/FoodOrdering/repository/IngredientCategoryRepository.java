package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {

    Optional<List<IngredientCategory>> findByRestaurantId(Long restaurantId);
}
