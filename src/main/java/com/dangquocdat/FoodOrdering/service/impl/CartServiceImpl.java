package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.cart.CartDto;
import com.dangquocdat.FoodOrdering.dto.cart.CartItemDto;
import com.dangquocdat.FoodOrdering.dto.cart.request.CartItemAdditionRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.Cart;
import com.dangquocdat.FoodOrdering.entity.CartItem;
import com.dangquocdat.FoodOrdering.entity.Food;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.CartItemRepository;
import com.dangquocdat.FoodOrdering.repository.CartRepository;
import com.dangquocdat.FoodOrdering.repository.FoodRepository;
import com.dangquocdat.FoodOrdering.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodRepository foodRepository;
    private final ModelMapper modelMapper;


    @Override
    public CartItemDto addItemToCart(CartItemAdditionRequest request, UserDto userDto) {

        Food food = foodRepository.findById(request.getFoodId())
                        .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+request.getFoodId()));

        Cart cart = cartRepository.findByCustomerId(userDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given customer id: "+userDto.getId()));

        // if food of cartItem is already existed in cart -> update quantity
        for(CartItem cartItem : cart.getCartItems()) {
            if(cartItem.getFood().equals(food))
            {
                return updateCartItemQuantity(cartItem.getId(), request.getQuantity(), userDto);
            }
        }

        // create new CartItem
        CartItem cartItem = new CartItem();
        cartItem.setFood(food);
        cartItem.setCart(cart);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setIngredients(request.getIngredients());
        cartItem.setTotalPrice(food.getPrice()*request.getQuantity());

        // save cartItem to DB
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        CartItemDto cartItemDto = modelMapper.map(savedCartItem, CartItemDto.class);
        cartItemDto.setCartId(savedCartItem.getCart().getId());

        cart.getCartItems().add(savedCartItem);

        // save cart total price
        double cartTotalPrice = calculateCartTotalPrice(cart);
        cart.setTotalPrice(cartTotalPrice);
        cartRepository.save(cart);

        return cartItemDto;
    }

    @Override
    public CartItemDto updateCartItemQuantity(Long cartItemId, int quantity, UserDto userDto) {

        Cart cart = cartRepository.findByCustomerId(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given customer id: "+userDto.getId()));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                                .orElseThrow(() -> new ResourceNotFoundException("CartItem is not exists with given id: "+cartItemId));

        int newQuantity = cartItem.getQuantity()+quantity;
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(cartItem.getFood().getPrice()*newQuantity);

        // save cartItem to DB:
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        CartItemDto cartItemDto = modelMapper.map(savedCartItem, CartItemDto.class);
        cartItemDto.setCartId(savedCartItem.getCart().getId());

        // save cart total price
        double cartTotalPrice = calculateCartTotalPrice(cart);
        cart.setTotalPrice(cartTotalPrice);
        cartRepository.save(cart);


        return cartItemDto;
    }

    @Override
    public CartDto removeItemFromCart(Long cartItemId, UserDto userDto) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                                .orElseThrow(() -> new ResourceNotFoundException("CartItem is not exists with given id: "+cartItemId));

        Cart cart = cartRepository.findByCustomerId(userDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given customer id: "+userDto.getId()));

        cart.getCartItems().remove(cartItem);

        Cart savedCart = cartRepository.save(cart);

        CartDto cartDto = modelMapper.map(savedCart, CartDto.class);
        cartDto.setCustomerId(userDto.getId());

        // save cart total price
        double cartTotalPrice = calculateCartTotalPrice(cart);
        cart.setTotalPrice(cartTotalPrice);
        cartRepository.save(cart);

        return cartDto;
    }

    @Override
    public double calculateCartTotalPrice(Cart cart) {

        double cartTotalPrice = 0;
        List<CartItem> cartItems = cart.getCartItems();

        for(CartItem item : cartItems) {
            cartTotalPrice += item.getTotalPrice();
        }

        return cartTotalPrice;
    }

    @Override
    public CartDto findCartById(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given id: "+cartId));

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setCustomerId(cart.getCustomer().getId());

        return cartDto;
    }

    @Override
    public CartDto findCartByCustomerId(Long customerId) {

        Cart cart = cartRepository.findByCustomerId(customerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given customer id: "+customerId));

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setCustomerId(cart.getCustomer().getId());


        // save cart total price
        double cartTotalPrice = calculateCartTotalPrice(cart);
        cart.setTotalPrice(cartTotalPrice);
        cartRepository.save(cart);

        return cartDto;
    }

    @Override
    public CartDto clearCart(Long customerId) {

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given customer id: "+customerId));

        cart.getCartItems().clear();

        Cart savedCart = cartRepository.save(cart);

        CartDto cartDto = modelMapper.map(savedCart, CartDto.class);
        cartDto.setCustomerId(customerId);

        return cartDto;
    }
}
