package com.dangquocdat.FoodOrdering.dto.auth.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordResponse {

    String email;
    String resetPasswordTicket;
}
