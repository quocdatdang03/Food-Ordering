package com.dangquocdat.FoodOrdering.dto.ingredient;


import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientCategoryDto {

    Long id;
    String name;
    List<IngredientItemDto> ingredients;
}
