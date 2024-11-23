package com.dangquocdat.FoodOrdering.dto.food.request;

import com.dangquocdat.FoodOrdering.dto.category.CategoryDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.entity.Category;
import com.dangquocdat.FoodOrdering.entity.IngredientsItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodCreationRequest {

    String name;
    String description;
    double price;
    List<String> images;
    boolean available = true;

    @JsonProperty("isVegetarian")
    boolean isVegetarian;

    @JsonProperty("isSeasonal")
    boolean isSeasonal;
    Long categoryId;
    Long restaurantId;
    List<IngredientItemDto> ingredients;
}
