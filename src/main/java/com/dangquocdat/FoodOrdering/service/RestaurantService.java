package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.restaurant.request.RestaurantCreationRequest;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.User;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantCreationRequest restaurantRequest, UserDto owner);

    RestaurantResponse updateRestaurant(Long restaurantId, RestaurantCreationRequest restaurantRequest);
}
