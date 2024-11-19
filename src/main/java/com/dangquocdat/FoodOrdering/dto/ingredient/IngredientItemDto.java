package com.dangquocdat.FoodOrdering.dto.ingredient;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientItemDto {

    Long id;
    String name;
    boolean inStock;
    IngredientCategoryDto category;
}
