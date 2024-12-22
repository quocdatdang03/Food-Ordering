package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.category.CategoryDto;
import com.dangquocdat.FoodOrdering.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<CategoryDto>> getCategoriesOfRestaurant(@PathVariable("restaurantId") Long restaurantId) {

        return ResponseEntity
                .ok(categoryService.getCategoriesByRestaurantId(restaurantId));
    }

}
