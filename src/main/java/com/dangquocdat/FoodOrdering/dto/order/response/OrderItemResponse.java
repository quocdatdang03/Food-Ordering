package com.dangquocdat.FoodOrdering.dto.order.response;


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
    Long foodId;
    Long orderId;
    int quantity;
    double totalPrice;
    List<String> ingredients;
}
