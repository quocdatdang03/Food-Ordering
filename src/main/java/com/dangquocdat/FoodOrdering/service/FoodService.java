package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.food.request.FoodCreationRequest;
import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;

import java.util.List;

public interface FoodService {

    // ADMIN
    FoodResponse createFood(FoodCreationRequest foodCreationRequest);

    // ADMIN
    FoodResponse updateFood(Long foodId, FoodCreationRequest foodCreationRequest);

    // ADMIN
    String deleteFoodFromRestaurant(Long foodId);

    List<FoodResponse> filterRestaurantsFood(
            Long restaurantId, boolean isVegetarian, boolean isNonVegetarian, boolean isSeasonal, String foodCategory
    );

    List<FoodResponse> searchFood(String keyword);

    FoodResponse findFoodById(Long foodId);

    // ADMIN
    FoodResponse updateAvailableStatus(Long foodId);
}
