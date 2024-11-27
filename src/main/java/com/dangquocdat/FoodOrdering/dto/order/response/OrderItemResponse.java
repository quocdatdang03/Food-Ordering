package com.dangquocdat.FoodOrdering.dto.order.response;


import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {

    Long id;
    FoodResponse food;
    Long orderId;
    int quantity;
    double totalPrice;
    List<String> ingredients;
}
