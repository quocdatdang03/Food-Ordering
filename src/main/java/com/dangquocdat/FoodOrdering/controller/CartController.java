package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.cart.CartDto;
import com.dangquocdat.FoodOrdering.dto.cart.CartItemDto;
import com.dangquocdat.FoodOrdering.dto.cart.request.CartItemAdditionRequest;
import com.dangquocdat.FoodOrdering.dto.cart.request.CartItemUpdateRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.service.CartService;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CartDto> getCartOfCustomer(
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(cartService.findCartByCustomerId(userDto.getId()));
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addItemToCart(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CartItemAdditionRequest cartItemAdditionRequest
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cartService.addItemToCart(cartItemAdditionRequest, userDto));
    }

    @PutMapping("/cartItem")
    public ResponseEntity<CartItemDto> updateCartItemQuantity(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CartItemUpdateRequest cartItemUpdateRequest
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(cartService.updateCartItemQuantity(cartItemUpdateRequest.getId(), cartItemUpdateRequest.getQuantity()));
    }

    @DeleteMapping("cart-item/{id}")
    public ResponseEntity<CartDto> removeItemFromCart(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("id") Long cartItemId
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(cartService.removeItemFromCart(cartItemId, userDto));
    }

    @DeleteMapping
    public ResponseEntity<CartDto> clearCart(
            @RequestHeader("Authorization") String jwtToken
    ) {

        UserDto userDto = getUserDtoByJwtToken(jwtToken);

        return ResponseEntity.ok(cartService.clearCart(userDto.getId()));
    }

    private UserDto getUserDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);
        UserDto userDto = userService.findByJwtToken(onlyToken);

        return userDto;
    }
}
