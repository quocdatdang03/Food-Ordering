package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.restaurant.RestaurantFavoriteDto;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.RestaurantService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @PathVariable("id") Long restaurantId,
            @RequestHeader("Authorization") String jwtToken
    ) {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurant(
            @RequestParam("keyword") String keyword,
            @RequestHeader("Authorization") String jwtToken
    ) {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(restaurantService.searchRestaurant(keyword));
    }

    @PutMapping("/{id}/favorites")
    public ResponseEntity<RestaurantFavoriteDto> addToFavorites(
            @PathVariable("id") Long restaurantId,
            @RequestHeader("Authorization") String jwtToken
    ) {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(restaurantService.addToFavorites(restaurantId, userDto));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
