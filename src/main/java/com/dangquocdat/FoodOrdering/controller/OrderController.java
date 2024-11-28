package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.order.request.OrderRequest;
import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.payment.PaymentResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.OrderService;
import com.dangquocdat.FoodOrdering.service.PaymentService;
import com.dangquocdat.FoodOrdering.service.UserService;
import com.stripe.exception.StripeException;
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
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody OrderRequest orderRequest
    ) throws StripeException {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        OrderResponse orderResponse = orderService.createOrder(orderRequest, userDto);

        PaymentResponse paymentResponse = paymentService.createPaymentLink(orderResponse);

        return ResponseEntity.ok(paymentResponse);
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
