package com.dangquocdat.FoodOrdering.dto.ingredient.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientCategoryCreationRequest {

    String name;
    Long restaurantId;
}
