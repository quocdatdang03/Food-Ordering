package com.dangquocdat.FoodOrdering.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException{

    private HttpStatus httpStatus;
    private String message;
}
