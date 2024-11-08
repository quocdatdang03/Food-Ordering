package com.dangquocdat.FoodOrdering.service;


import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.User;

public interface UserService {

    UserDto findByJwtToken(String jwtToken);
    UserDto findByEmail(String email);
}
