package com.dangquocdat.FoodOrdering.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="ingredient_categories")
public class IngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="restaurant_id")
    Restaurant restaurant;

    @OneToMany(mappedBy = "category")
    List<IngredientsItem> ingredients = new ArrayList<>();
}
