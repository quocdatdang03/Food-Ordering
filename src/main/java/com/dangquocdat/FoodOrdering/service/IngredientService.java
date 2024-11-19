package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientCategoryDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientCategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientItemCreationRequest;

import java.util.List;

public interface IngredientService {

    IngredientCategoryDto createIngredientCategory(IngredientCategoryCreationRequest ingredientCategoryCreationRequest);

    IngredientCategoryDto getIngredientCategoryById(Long id);

    List<IngredientCategoryDto> getIngredientCategoryByRestaurantId(Long restaurantId);

    IngredientItemDto createIngredientItem(IngredientItemCreationRequest ingredientItemCreationRequest);

    List<IngredientItemDto> getIngredientItemByRestaurantId(Long restaurantId);

    IngredientItemDto updateStock(Long id);
}
