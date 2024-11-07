package com.dangquocdat.FoodOrdering.entity;


import com.dangquocdat.FoodOrdering.dto.RestaurantDto;
import com.dangquocdat.FoodOrdering.enums.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String fullName;

    String email;

    String password;

    USER_ROLE role;

    @JsonIgnore // whenever fetching user we don't need to include orders
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    List<Order> orders = new ArrayList<>();

    // @ElementCollection : it will create secondary table (name : user_favorites and have foreign key user_id linking User table,
    // and all fields of Restaurant Dto)
    @ElementCollection // store a collection of simple values related to current entity without creating a separate entity
    List<RestaurantDto> favorites = new ArrayList(); // list of favorites restaurant

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Address> addresses = new ArrayList<>();


}
