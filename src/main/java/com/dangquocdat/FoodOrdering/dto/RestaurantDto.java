package com.dangquocdat.FoodOrdering.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable // indicate that can be embedded to User entity
public class RestaurantDto {

    Long id;

    String title;

    String description;

    @Column(length = 1000)
    List<String> images;


}
