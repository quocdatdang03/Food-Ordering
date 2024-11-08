package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.exception.ApiException;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.UserRepository;
import com.dangquocdat.FoodOrdering.security.jwt.JwtTokenProvider;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Override
    public UserDto findByJwtToken(String jwtToken) {

        String email = jwtTokenProvider.getUsername(jwtToken);

        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Email is not exists in DB!"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto findByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Email is not exists in DB!"));

        return modelMapper.map(user, UserDto.class);
    }
}
