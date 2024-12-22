package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientCategoryDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientCategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientItemCreationRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.IngredientService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
@RequiredArgsConstructor
public class AdminIngredientController {

    private final IngredientService ingredientService;
    private final UserService userService;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategoryDto> createIngredientCategory(
            @RequestBody IngredientCategoryCreationRequest ingredientCategoryCreationRequest
    )
    {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ingredientService.createIngredientCategory(ingredientCategoryCreationRequest));
    }

    @PostMapping
    public ResponseEntity<IngredientItemDto> createIngredientItem(
            @RequestBody IngredientItemCreationRequest ingredientItemCreationRequest
    )
    {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ingredientService.createIngredientItem(ingredientItemCreationRequest));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<IngredientCategoryDto> getIngredientCategoryOfCurrentRestaurant(
            @PathVariable("id") Long ingredientCategoryId,
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(ingredientService.getIngredientCategoryByIdAndRestaurantId(ingredientCategoryId, userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientItemDto> getIngredientItemOfCurrentRestaurant(
            @PathVariable("id") Long ingredientItemId,
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(ingredientService.getIngredientByIdAndRestaurantId(ingredientItemId, userDto));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<IngredientCategoryDto> updateIngredientCategory(
            @PathVariable("id") Long ingredientCategoryId,
            @RequestBody IngredientCategoryCreationRequest ingredientCategoryUpdateRequest,
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(ingredientService.updateIngredientCategoryByIdAndRestaurantId(ingredientCategoryId, ingredientCategoryUpdateRequest, userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientItemDto> updateIngredientItem(
            @PathVariable("id") Long ingredientItemId,
            @RequestBody IngredientItemCreationRequest ingredientItemUpdateRequest,
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(ingredientService.updateIngredientItemByIdAndRestaurantId(ingredientItemId, ingredientItemUpdateRequest, userDto));
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategoryDto>> getIngredientCategoryByRestaurantId(
            @PathVariable("id") Long restaurantId
    ) {

        return ResponseEntity.ok(ingredientService.getIngredientCategoryByRestaurantId(restaurantId));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientItemDto>> getIngredientItemByRestaurantId(
            @PathVariable("id") Long restaurantId
    ) {

        return ResponseEntity.ok(ingredientService.getIngredientItemByRestaurantId(restaurantId));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<IngredientItemDto> updateStock(
            @PathVariable("id") Long ingredientItemId
    ) {

        return ResponseEntity.ok(ingredientService.updateStock(ingredientItemId));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
