package com.dangquocdat.FoodOrdering.controller;

import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> showProfile(@RequestHeader("Authorization") String jwtToken) {

        String onlyJwtToken = jwtToken.substring(7);

        UserDto userDto = userService.findByJwtToken(onlyJwtToken);

        return ResponseEntity.ok(userDto);
    }

}
