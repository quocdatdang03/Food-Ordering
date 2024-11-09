package com.dangquocdat.FoodOrdering.entity;


import com.dangquocdat.FoodOrdering.dto.restaurant.RestaurantFavoriteDto;
import com.dangquocdat.FoodOrdering.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.ArrayList;
import java.util.List;

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

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    UserRoleEnum role = UserRoleEnum.ROLE_CUSTOMER;

    @JsonIgnore // whenever fetching user we don't need to include orders
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    List<Order> orders = new ArrayList<>();

    // @ElementCollection : it will create secondary table (name : user_favorites and have foreign key user_id linking User table,
    // and all fields of RestaurantFavoriteDto)
    @ElementCollection // store a collection of simple values related to current entity without creating a separate entity
    List<RestaurantFavoriteDto> favorites = new ArrayList(); // list of favorites restaurant

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Address> addresses = new ArrayList<>();


}
