package com.dangquocdat.FoodOrdering.controller;


import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.FoodService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFoodById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("id") Long foodId
    )
    {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(foodService.findFoodById(foodId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<FoodResponse>> filterFoodOfRestaurant(
            @RequestHeader(value = "Authorization", required = false) String jwtToken,
            @PathVariable(value = "restaurantId", required = false) Long restaurantId,
            @RequestParam(value = "isVegetarian", required = false) boolean isVegetarian,
            @RequestParam(value = "isNonVegetarian", required = false) boolean isNonVegetarian,
            @RequestParam(value = "isSeasonal", required = false) boolean isSeasonal,
            @RequestParam(value="foodCategory", required = false) String foodCategory
    )
    {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(
                foodService.filterRestaurantsFood(restaurantId, isVegetarian, isNonVegetarian, isSeasonal, foodCategory)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> searchFoodByKeyword(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam("keyword") String keyword
    )
    {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(foodService.searchFood(keyword));
    }


    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
