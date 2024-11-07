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
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long totalAmount;

    String orderStatus;

    Date createdDate;

    int totalItem;

    double totalPrice;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    User customer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;

    //Payment payment;


}
