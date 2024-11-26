package com.dangquocdat.FoodOrdering.dto.order.response;

import com.dangquocdat.FoodOrdering.entity.Address;
import com.dangquocdat.FoodOrdering.entity.OrderItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    Long id;
    int totalItem;
    Date createdDate;
    double totalPrice;
    String orderStatus;
    Address deliveryAddress;
    Long customerId;
    Long restaurantId;
    List<OrderItem> orderItems;
}
