package com.dangquocdat.FoodOrdering.dto.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable // indicate that can be embedded to User entity
public class RestaurantFavoriteDto {

    Long id;

    String name;

    String description;

    @Column(length = 1000)
    List<String> images;

    boolean open;



}
