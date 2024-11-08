package com.dangquocdat.FoodOrdering.dto.auth.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    String accessToken;
    String message;
    String tokenType = "Bearer";
    String role;
}
