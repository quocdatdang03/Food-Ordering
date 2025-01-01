package com.dangquocdat.FoodOrdering.dto.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyUserDto {

    String email;
    String verificationCode;
}
