package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.food.request.FoodCreationRequest;
import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.FoodService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/foods")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<FoodResponse> createFood(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody FoodCreationRequest foodCreationRequest)
    {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(foodService.createFood(foodCreationRequest));

    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponse> updateFood(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("id") Long foodId,
            @RequestBody FoodCreationRequest foodCreationRequest
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(foodService.updateFood(foodId, foodCreationRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("id") Long foodId
    )
    {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(foodService.deleteFoodFromRestaurant(foodId));
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<FoodResponse> updateAvailableStatus(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("id") Long foodId
    )
    {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(foodService.updateAvailableStatus(foodId));
    }


    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
