package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.IngredientCategory;
import com.dangquocdat.FoodOrdering.entity.IngredientsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientItemRepository extends JpaRepository<IngredientsItem, Long> {

    Optional<List<IngredientsItem>> findByRestaurantId(Long restaurantId);

    Optional<IngredientsItem> findByIdAndRestaurantId(Long ingredientItemId, Long restaurantId);
}
