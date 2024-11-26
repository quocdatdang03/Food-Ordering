package com.dangquocdat.FoodOrdering.dto.cart;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDto {

    Long id;

    Long customerId;

    double totalPrice;

    List<CartItemDto> cartItems;
}
