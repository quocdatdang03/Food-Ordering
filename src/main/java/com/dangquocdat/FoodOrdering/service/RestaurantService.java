package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.restaurant.RestaurantFavoriteDto;
import com.dangquocdat.FoodOrdering.dto.restaurant.request.RestaurantCreationRequest;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantCreationRequest restaurantRequest, UserDto owner);

    RestaurantResponse updateRestaurant(Long restaurantId, RestaurantCreationRequest restaurantRequest);

    String deleteRestaurant(Long restaurantId);

    List<RestaurantResponse> getAllRestaurants();

    List<RestaurantResponse> searchRestaurant(String keyword);

    RestaurantResponse getRestaurantById(Long restaurantId);

    RestaurantResponse getRestaurantByOwnerId(Long ownerId);

    RestaurantFavoriteDto addToFavorites(Long restaurantId, UserDto user);

    RestaurantResponse updateRestaurantStatus(Long restaurantId);
}
