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
@RequestMapping("/api/admin/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

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

    @PatchMapping("/{id}/stock")
    public ResponseEntity<IngredientItemDto> updateStock(
            @PathVariable("id") Long ingredientItemId
    ) {

        return ResponseEntity.ok(ingredientService.updateStock(ingredientItemId));
    }
}
