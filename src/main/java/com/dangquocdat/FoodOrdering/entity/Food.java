package com.dangquocdat.FoodOrdering.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Entity
@Table(name="foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    double price;

    @ElementCollection // it will automatically create table food_images
    @Column(length = 1000)
    List<String> images;

    boolean available;

    boolean isVegetarian;

    boolean isSeasonal;

    Date creationDate;

    @ManyToOne
    @JoinColumn(name="category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    Restaurant restaurant;

    @ManyToMany
    List<IngredientsItem> ingredientsItems = new ArrayList<>();



}
