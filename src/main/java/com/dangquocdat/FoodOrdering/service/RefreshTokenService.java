package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.entity.RefreshToken;
import com.dangquocdat.FoodOrdering.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String userName);
    RefreshToken verifyExpirationOfToken(RefreshToken refreshToken);
    boolean checkExistedRefreshToken(User user);
}
