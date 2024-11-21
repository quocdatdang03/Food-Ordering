package com.dangquocdat.FoodOrdering.dto.restaurant.response;

import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantResponse {

    Long id;
    String name;
    String description;
    String cuisineType;
    ContactInformation contactInformation;
    String openingHours;
    List<String> images;
    Address address;
    LocalDateTime registrationDate;
    boolean open;
    UserDto owner;
    List<FoodResponse> foods = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
}
