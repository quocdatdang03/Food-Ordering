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
@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    int quantity;

    Long totalPrice;

    List<String> ingredients;

    @ManyToOne
    @JoinColumn(name="order_id")
    Order order;

}
