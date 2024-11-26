package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.order.request.OrderRequest;
import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.OrderService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody OrderRequest orderRequest
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest, userDto));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<OrderResponse>> getCustomerOrdersHistory(
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(orderService.getCustomerOrders(userDto.getId()));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }
}
