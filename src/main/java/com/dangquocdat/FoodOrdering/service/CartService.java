package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.cart.CartDto;
import com.dangquocdat.FoodOrdering.dto.cart.CartItemDto;
import com.dangquocdat.FoodOrdering.dto.cart.request.CartItemAdditionRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.Cart;

public interface CartService {

    CartItemDto addItemToCart(CartItemAdditionRequest request, UserDto userDto);

    CartItemDto updateCartItemQuantity(Long cartItemId, int quantity, UserDto userDto);

    CartDto removeItemFromCart(Long cartItemId, UserDto userDto);

    double calculateCartTotalPrice(Cart cart);

    CartDto findCartById(Long cartId);

    CartDto findCartByCustomerId(Long customerId);

    CartDto clearCart(Long customerId);

}
