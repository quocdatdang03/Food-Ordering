package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.auth.VerifyUserDto;
import com.dangquocdat.FoodOrdering.dto.auth.request.*;
import com.dangquocdat.FoodOrdering.dto.auth.response.AuthResponse;
import com.dangquocdat.FoodOrdering.dto.auth.response.ResetPasswordResponse;

public interface AuthService {

    AuthResponse register(RegistrationRequest registrationRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshJwtToken(RefreshTokenRequest refreshTokenRequest);
    void verifyUser(VerifyUserDto verifyUserDto);
    void resendVerificationCode(String email);
    String sendEmailResetPassword(String email);
    String resetPassword(ResetPasswordRequest resetPasswordRequest);
    ResetPasswordResponse getUserResetPasswordInfo(ResetPasswordInfoRequest request);
}
