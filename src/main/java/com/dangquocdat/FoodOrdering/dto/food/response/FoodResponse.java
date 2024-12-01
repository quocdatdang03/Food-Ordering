package com.dangquocdat.FoodOrdering.dto.food.response;

import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodResponse {

    Long id;
    String name;
    String description;
    double price;
    List<String> images;
    boolean available;
    boolean isVegetarian;
    boolean isSeasonal;
    Category category;
    Long restaurantId;
    String restaurantName;
    List<IngredientItemDto> ingredients;
}
