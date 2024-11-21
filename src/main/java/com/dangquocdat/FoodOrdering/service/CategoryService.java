package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.category.request.CategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryCreationRequest categoryCreationRequest, Long ownerId);

    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getCategoriesByRestaurantId(Long restaurantId);

}
