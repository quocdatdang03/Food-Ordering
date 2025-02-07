package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.order.request.OrderRequest;
import com.dangquocdat.FoodOrdering.dto.order.response.OrderItemResponse;
import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.*;
import com.dangquocdat.FoodOrdering.exception.ApiException;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.*;
import com.dangquocdat.FoodOrdering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RestaurantRepository restaurantRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, UserDto userDto) {

        User customer = userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("User is not exists with given id: "+userDto.getId()));

        Restaurant restaurant = restaurantRepository.findById(orderRequest.getRestaurantId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+orderRequest.getRestaurantId()));

        // save address to DB:
        Address address = orderRequest.getDeliveryAddress();
        addressRepository.save(address);

        // check if user address is not already existed -> add to users_addresses
        if(!customer.getAddresses().contains(address))
        {
            customer.getAddresses().add(address);

            userRepository.save(customer);
        }

        Cart cart = cartRepository.findByCustomerId(userDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart is not exists with given customer id: "+userDto.getId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderStatus("PENDING");
        order.setDeliveryAddress(address);
        order.setTotalPrice(cart.getTotalPrice());
        order.setRestaurant(restaurant);
        order.setCreatedDate(new Date());

        // save order to db
        Order savedOrder = orderRepository.save(order);

        // create order items and save to DB
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cart.getCartItems())
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setOrder(savedOrder);

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);

            orderItems.add(savedOrderItem);
        }

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalItem(orderItems.size());

        orderRepository.save(savedOrder);

        restaurant.getOrders().add(savedOrder);
        customer.getOrders().add(savedOrder);

        return getOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String orderStatus) {

        Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order is not exists with given id: "+orderId));

        if(orderStatus.equals("OUT_OF_STOCK")
                || orderStatus.equals("PENDING")
                || orderStatus.equals("DELIVERED")
                || orderStatus.equals("COMPLETED"))
        {
            // update order status
            order.setOrderStatus(orderStatus);

            Order savedOrder = orderRepository.save(order);

            return getOrderResponse(savedOrder);
        }

        throw new ApiException(HttpStatus.BAD_REQUEST, "Order status is invalid!");
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order is not exists with given id: " + orderId));

        orderRepository.delete(order);
    }

        @Override
    public List<OrderResponse> getCustomerOrders(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId)
                         .orElseThrow(() -> new ResourceNotFoundException("Order is not exists with given customer id: " + customerId));

        List<OrderResponse> orderResponses =  orders.stream().map((order) -> {
            return getOrderResponse(order);
        }).collect(Collectors.toList());

        return orderResponses;

    }

    // THIS METHOD IS ALSO FOR FILTERING BY orderStatus
    @Override
    public List<OrderResponse> getRestaurantOrders(Long restaurantId, String orderStatus) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order is not exists with given restaurant id: " + restaurantId));

        // filter order by orderStatus
        if(orderStatus!=null && !orderStatus.isEmpty())
        {
            orders = orders.stream().filter((item) -> item.getOrderStatus().equals(orderStatus))
                        .collect(Collectors.toList());
        }

        return orders.stream().map((order) -> {
            return getOrderResponse(order);
        }).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderByOrderIdAndRestaurantId(Long orderId, Long restaurantId) {

        Order order = orderRepository.findByIdAndRestaurantId(orderId, restaurantId)
                        .orElseThrow(() -> new ResourceNotFoundException(("Order is not exists with given id: "+orderId+" or  restaurant id: "+restaurantId)));

        return getOrderResponse(order);
    }

    private OrderResponse getOrderResponse(Order order) {
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setRestaurantId(order.getRestaurant().getId());
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream().map((item) -> {

            OrderItemResponse orderItemResponse = modelMapper.map(item, OrderItemResponse.class);
            orderItemResponse.setOrderId(item.getOrder().getId());

            return orderItemResponse;
        }).collect(Collectors.toList());

        orderResponse.setOrderItems(orderItemResponses);

        return orderResponse;
    }
}
