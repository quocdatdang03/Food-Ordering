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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(
            @PathVariable("id") Long restaurantId
    ) {

       return ResponseEntity.ok(restaurantService.deleteRestaurant(restaurantId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantResponse> updateRestaurantStatus(
            @PathVariable("id") Long restaurantId,
            @RequestHeader("Authorization") String jwtToken
    ) {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(restaurantService.updateRestaurantStatus(restaurantId));
    }

    @GetMapping("/user")
    public ResponseEntity<RestaurantResponse> getRestaurantByOwnerId(
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(restaurantService.getRestaurantByOwnerId(userDto.getId()));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
