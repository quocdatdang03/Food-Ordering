package com.dangquocdat.FoodOrdering.dto.food.request;

import com.dangquocdat.FoodOrdering.entity.Category;
import com.dangquocdat.FoodOrdering.entity.IngredientsItem;
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
    boolean available;
    boolean isVegetarian;
    boolean isSeasonal;
    Category category;
    Long restaurantId;
    List<IngredientsItem> ingredients;
}
