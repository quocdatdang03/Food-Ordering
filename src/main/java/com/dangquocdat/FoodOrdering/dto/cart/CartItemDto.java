package com.dangquocdat.FoodOrdering.dto.cart;

import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDto {

    Long id;

    FoodResponse food;

    int quantity;

    double totalPrice;

    List<String> ingredients;

    Long cartId;

}
