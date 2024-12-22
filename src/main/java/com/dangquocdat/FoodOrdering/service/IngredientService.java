package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientCategoryDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientCategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientItemCreationRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;

import java.util.List;

public interface IngredientService {

    IngredientCategoryDto createIngredientCategory(IngredientCategoryCreationRequest ingredientCategoryCreationRequest);

    IngredientCategoryDto getIngredientCategoryById(Long id);
    IngredientCategoryDto getIngredientCategoryByIdAndRestaurantId(Long id, UserDto userDto);
    IngredientItemDto getIngredientByIdAndRestaurantId(Long id, UserDto userDto);

    IngredientCategoryDto updateIngredientCategoryByIdAndRestaurantId(Long ingredientCategoryId, IngredientCategoryCreationRequest ingredientCategoryUpdateRequest, UserDto userDto);
    IngredientItemDto updateIngredientItemByIdAndRestaurantId(Long ingredientItemId, IngredientItemCreationRequest ingredientItemUpdateRequest, UserDto userDto);

    List<IngredientCategoryDto> getIngredientCategoryByRestaurantId(Long restaurantId);

    IngredientItemDto createIngredientItem(IngredientItemCreationRequest ingredientItemCreationRequest);

    List<IngredientItemDto> getIngredientItemByRestaurantId(Long restaurantId);

    IngredientItemDto updateStock(Long id);
}
