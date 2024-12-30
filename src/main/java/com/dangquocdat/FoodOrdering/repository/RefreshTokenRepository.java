package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
    RefreshToken findByUserId(Long userId);

    @Transactional
    @Modifying
    void deleteByUserId(Long userId);

}
