package com.dangquocdat.FoodOrdering.dto.order.request;

import com.dangquocdat.FoodOrdering.entity.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    Long restaurantId;
    Address deliveryAddress;
}
