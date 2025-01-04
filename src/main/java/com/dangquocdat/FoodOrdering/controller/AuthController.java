package com.dangquocdat.FoodOrdering.controller;


import com.dangquocdat.FoodOrdering.dto.auth.VerifyUserDto;
import com.dangquocdat.FoodOrdering.dto.auth.request.*;
import com.dangquocdat.FoodOrdering.dto.auth.response.AuthResponse;
import com.dangquocdat.FoodOrdering.dto.auth.response.ResetPasswordResponse;
import com.dangquocdat.FoodOrdering.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUserAccount(@RequestBody RegistrationRequest registrationRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity
                .ok(authService.login(loginRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {

        return ResponseEntity.ok(authService.refreshJwtToken(refreshTokenRequest));
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verifyAccount(@RequestBody VerifyUserDto verifyUserDto) {

        authService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/resendVerification")
    public ResponseEntity<String> resendVerificationCode(@RequestParam("email") String email) {

        authService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code has been sent");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestParam("email") String email) {

        return ResponseEntity.ok(authService.sendEmailResetPassword(email));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResetPasswordResponse> verifyResetPasswordInfo(@RequestBody ResetPasswordInfoRequest request) {

        return ResponseEntity.ok(authService.getUserResetPasswordInfo(request));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest));
    }
}
