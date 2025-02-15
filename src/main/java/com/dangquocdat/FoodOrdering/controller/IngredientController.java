package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientCategoryDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientCategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientItemCreationRequest;
import com.dangquocdat.FoodOrdering.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/category/{id}")
    public ResponseEntity<IngredientCategoryDto> getIngredientCategoryById(
            @PathVariable("id") Long ingredientCategoryId
    ) {

        return ResponseEntity.ok(ingredientService.getIngredientCategoryById(ingredientCategoryId));
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

}
