package com.dangquocdat.FoodOrdering.dto.auth.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {

    String fullName;
    String email; //username
    String password;
    String role;

}
