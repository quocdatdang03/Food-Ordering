package com.dangquocdat.FoodOrdering.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    User owner;

    String name;

    String description;

    String cuisineType;

    @Embedded // all fields of ContactInformation class will be embedded (available) in here
    ContactInformation contactInformation;

    String openingHours;

    LocalDateTime registrationDate;

    boolean open;

    @ElementCollection
    @Column(length = 1000)
    List<String> images;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Food> foods = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "restaurant_address_id")
    Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Order> orders = new ArrayList<>();
}

