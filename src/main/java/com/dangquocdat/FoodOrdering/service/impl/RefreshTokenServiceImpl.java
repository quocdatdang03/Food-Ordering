package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.entity.RefreshToken;
import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.exception.ApiException;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.RefreshTokenRepository;
import com.dangquocdat.FoodOrdering.repository.UserRepository;
import com.dangquocdat.FoodOrdering.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(String userName) {

        // get user by email
        User user = userRepository.findByEmail(userName)
                        .orElseThrow(() -> new ResourceNotFoundException("User is not exists with given userName: "+userName));

        // create refreshToken:
        RefreshToken refreshToken = RefreshToken.builder()
                                            .token(UUID.randomUUID().toString())
                                            .expiryDate(Instant.now().plusMillis(1000*60*10)) // 2 minutes
                                            .user(user)
                                            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // handle refresh jwt token:
    @Override
    public RefreshToken verifyExpirationOfToken(RefreshToken refreshToken) {

        // if refreshToken  expired -> delete refreshToken in DB
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {

            refreshTokenRepository.delete(refreshToken);
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Refresh token was expired, Please make a new sign in request");
        }

        // else -> return refreshToken
        return refreshToken;
    }

    @Override
    public boolean checkExistedRefreshToken(User user) {

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());

        if(refreshToken!=null)
            return true;

        return false;
    }

}
