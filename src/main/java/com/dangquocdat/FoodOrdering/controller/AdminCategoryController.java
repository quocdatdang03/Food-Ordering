package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.category.request.CategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.category.CategoryDto;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.service.CategoryService;
import com.dangquocdat.FoodOrdering.service.RestaurantService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final RestaurantService restaurantService;


    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CategoryCreationRequest categoryCreationRequest
    ) {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(categoryCreationRequest, userDto.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("id") Long categoryId
    ) {
        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    // XEM XÉT LẠI ENDPOINT NÀY TRONG CASE của Customer
    // (Vì customer cũng có thể get category by restaurant id)
    @GetMapping("/restaurant")
    public ResponseEntity<List<CategoryDto>> getCategoryByRestaurant(
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);
        RestaurantResponse restaurantResponse = restaurantService.getRestaurantByOwnerId(userDto.getId());

        Long restaurantId = restaurantResponse.getId();

        return ResponseEntity.ok(categoryService.getCategoriesByRestaurantId(restaurantId));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
