package com.dangquocdat.FoodOrdering.dto.user;

import com.dangquocdat.FoodOrdering.dto.RestaurantDto;
import com.dangquocdat.FoodOrdering.entity.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;
    String fullName;
    String email;
    String role;
    List<RestaurantDto> favorites;
    List<Address> addresses;
}
