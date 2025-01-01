package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.auth.VerifyUserDto;
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
import com.dangquocdat.FoodOrdering.service.EmailService;
import com.dangquocdat.FoodOrdering.service.RefreshTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

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
    private final EmailService emailService;


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

        user.setEnable(false);
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(1));
        sendVerificationEmail(user);

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

        User userFromDB = userRepository.findByEmail(loginRequest.getEmail())
                            .orElseThrow(()-> new ApiException(HttpStatus.BAD_REQUEST, "User is not exists with given email: "+loginRequest.getEmail()));

        // check if account is not enabled -> throw error:
        if(!userFromDB.isEnable())
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Account not verified, Please verify your account");

        Authentication authentication = null;
        try {
            // authenticate -> will throw exception 401 (e.g BadCredentialsException) if username or password is not correct
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "User name or password is not correct");
        }

        // Save authentication info in SecurityContextHolder  for serving next request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        // get role:
        String role = null;

        role = userFromDB.getRole().name();

        // check if refresh token of current user is already existed in DB -> delete refreshToken
        if(refreshTokenService.checkExistedRefreshToken(userFromDB))
        {
            // delete refreshToken from DB
            refreshTokenRepository.deleteByUserId(userFromDB.getId());
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
                                    }).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "InvalidRefreshToken"));
    }

    @Override
    public void verifyUser(VerifyUserDto verifyUserDto) {
        User user = userRepository.findByEmail(verifyUserDto.getEmail())
                        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User not found"));

        if(user.getVerificationCodeExpiredAt().isBefore(LocalDateTime.now()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Verification code has expired");

        if(user.getVerificationCode().equals(verifyUserDto.getVerificationCode()))
        {
            user.setEnable(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiredAt(null);

            userRepository.save(user);
        }
        else
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid verification code");
    }

    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User not found"));

        if(user.isEnable())
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account is already verified");

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();

        int verificationCode = random.nextInt(900000)+100000;

        return String.valueOf(verificationCode);
    }


}
