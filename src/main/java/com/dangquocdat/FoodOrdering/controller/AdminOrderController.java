package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.OrderService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrdersHistory(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("restaurantId") Long restaurantId,
            @RequestParam(value = "order_status", required = false) String orderStatus
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurantId, orderStatus));
    }

    @PatchMapping("/{orderId}/{orderStatus}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderStatus") String orderStatus
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,orderStatus));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }

}
