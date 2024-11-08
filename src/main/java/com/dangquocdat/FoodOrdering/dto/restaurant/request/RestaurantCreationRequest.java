package com.dangquocdat.FoodOrdering.dto.restaurant.request;

import com.dangquocdat.FoodOrdering.entity.Address;
import com.dangquocdat.FoodOrdering.entity.ContactInformation;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCreationRequest {

    String name;
    String description;
    String cuisineType;
    ContactInformation contactInformation;
    String openingHours;
    List<String> images;
    Address address;
}
