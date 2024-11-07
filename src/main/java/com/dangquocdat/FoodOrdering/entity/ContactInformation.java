package com.dangquocdat.FoodOrdering.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactInformation {

    String email;
    String phoneNumber;
    String facebook;
    String instagram;
}
