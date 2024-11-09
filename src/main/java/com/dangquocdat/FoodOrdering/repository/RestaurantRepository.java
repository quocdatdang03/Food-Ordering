package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r WHERE CONCAT(r.name,' ',r.cuisineType) LIKE %?1%")
    List<Restaurant> searchByKeyword(String keyword);

    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = ?1")
    Optional<Restaurant> findByOwnerId(Long ownerId);
}
