package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.order.request.OrderRequest;
import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest, UserDto userDto);

    OrderResponse updateOrderStatus(Long orderId, String orderStatus);

    void cancelOrder(Long orderId);

    List<OrderResponse> getCustomerOrders(Long customerId);

    List<OrderResponse> getRestaurantOrders(Long restaurantId, String orderStatus);

    OrderResponse getOrderByOrderIdAndRestaurantId(Long orderId, Long restaurantId);
}
