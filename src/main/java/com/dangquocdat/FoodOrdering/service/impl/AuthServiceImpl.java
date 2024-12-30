package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.auth.request.LoginRequest;
import com.dangquocdat.FoodOrdering.dto.auth.request.RefreshTokenRequest;
import com.dangquocdat.FoodOrdering.dto.auth.request.RegistrationRequest;
import com.dangquocdat.FoodOrdering.dto.auth.response.AuthResponse;
import com.dangquocdat.FoodOrdering.entity.Cart;
import com.dangquocdat.FoodOrdering.entity.RefreshToken;
import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.enums.UserRoleEnum;
import com.dangquocdat.FoodOrdering.exception.ApiException;
import com.dangquocdat.FoodOrdering.repository.CartRepository;
import com.dangquocdat.FoodOrdering.repository.RefreshTokenRepository;
import com.dangquocdat.FoodOrdering.repository.UserRepository;
import com.dangquocdat.FoodOrdering.security.jwt.JwtTokenProvider;
import com.dangquocdat.FoodOrdering.service.AuthService;
import com.dangquocdat.FoodOrdering.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public AuthResponse register(RegistrationRequest registrationRequest) {

        if(userRepository.existsByEmail(registrationRequest.getEmail()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already used by another account");

        User user = new User();
        user.setFullName(registrationRequest.getFullName());
        user.setEmail(registrationRequest.getEmail());

        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(encodedPassword);

        UserRoleEnum role = UserRoleEnum.valueOf(registrationRequest.getRole());
        user.setRole(role);

        // save user info to DB
        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword()
        );

        // Save authentication info in SecurityContextHolder  for serving next request
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // create cart for user
        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("User Registered Successfully!");
        authResponse.setRole(savedUser.getRole().name());
        authResponse.setAccessToken(jwtToken);

        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        // authenticate -> will throw exception (e.g BadCredentialsException) if username or password is not correct
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Save authentication info in SecurityContextHolder  for serving next request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        // get role:
        String role = null;

        Optional<User> userFromDB = userRepository.findByEmail(loginRequest.getEmail());
        if(userFromDB.isPresent())
            role = userFromDB.get().getRole().name();

        // check if refresh token of current user is already existed in DB -> delete refreshToken
        if(refreshTokenService.checkExistedRefreshToken(userFromDB.get()))
        {
            // delete refreshToken from DB
            refreshTokenRepository.deleteByUserId(userFromDB.get().getId());
        }

        // create refreshToken:
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("User Logged-In Successfully!");
        authResponse.setRole(role);
        authResponse.setAccessToken(jwtToken);
        authResponse.setRefreshToken(refreshToken.getToken());

        return authResponse;
    }

    @Override
    public AuthResponse refreshJwtToken(RefreshTokenRequest refreshTokenRequest) {

       return refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                                    .map(refreshToken -> refreshTokenService.verifyExpirationOfToken(refreshToken)) // verify expiration of token
                                    .map(refreshToken -> refreshToken.getUser()) // get User info by refresh Token
                                    .map(user -> {
                                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                                user.getEmail(), user.getPassword()
                                        );
                                        String accessToken = jwtTokenProvider.generateToken(authentication);

                                        AuthResponse authResponse = new AuthResponse();
                                        authResponse.setAccessToken(accessToken);
                                        authResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());

                                        return authResponse;
                                    }).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid!"));
    }


}
