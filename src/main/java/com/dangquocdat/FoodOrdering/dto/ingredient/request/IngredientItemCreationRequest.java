package com.dangquocdat.FoodOrdering.dto.ingredient.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientItemCreationRequest {

    String name;
    Long categoryId;
    Long restaurantId;
}
