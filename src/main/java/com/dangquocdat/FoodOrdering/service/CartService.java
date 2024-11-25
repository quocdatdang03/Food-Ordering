package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.cart.CartDto;
import com.dangquocdat.FoodOrdering.dto.cart.CartItemDto;
import com.dangquocdat.FoodOrdering.dto.cart.request.CartItemAdditionRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;

public interface CartService {

    CartItemDto addItemToCart(CartItemAdditionRequest request, UserDto userDto);

    CartItemDto updateCartItemQuantity(Long cartItemId, int quantity);

    CartDto removeItemFromCart(Long cartItemId, UserDto userDto);

    double calculateCartTotalPrice(CartDto cartDto);

    CartDto findCartById(Long cartId);

    CartDto findCartByCustomerId(Long customerId);

    CartDto clearCart(Long customerId);

}
