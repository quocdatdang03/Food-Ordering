package com.dangquocdat.FoodOrdering.dto.cart.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemAdditionRequest {

    Long foodId;
    int quantity;
    List<String> ingredients;
}
