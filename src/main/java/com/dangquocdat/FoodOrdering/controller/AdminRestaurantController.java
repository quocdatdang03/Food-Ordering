package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.restaurant.request.RestaurantCreationRequest;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.security.jwt.JwtTokenProvider;
import com.dangquocdat.FoodOrdering.service.RestaurantService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @RequestBody RestaurantCreationRequest restaurantCreationRequest,
            @RequestHeader("Authorization") String jwtToken
    ) {
        RestaurantResponse restaurantResponse = restaurantService.createRestaurant(restaurantCreationRequest, getUserDtoByJwtToken(jwtToken));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable("id") Long restaurantId,
            @RequestBody RestaurantCreationRequest restaurantUpdateRequest
    ) {

        RestaurantResponse restaurantResponse = restaurantService.updateRestaurant(restaurantId, restaurantUpdateRequest);

        return ResponseEntity.ok(restaurantResponse);
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
