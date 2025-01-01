package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.auth.VerifyUserDto;
import com.dangquocdat.FoodOrdering.dto.auth.request.LoginRequest;
import com.dangquocdat.FoodOrdering.dto.auth.request.RefreshTokenRequest;
import com.dangquocdat.FoodOrdering.dto.auth.request.RegistrationRequest;
import com.dangquocdat.FoodOrdering.dto.auth.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegistrationRequest registrationRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshJwtToken(RefreshTokenRequest refreshTokenRequest);
    void verifyUser(VerifyUserDto verifyUserDto);
    void resendVerificationCode(String email);
}
