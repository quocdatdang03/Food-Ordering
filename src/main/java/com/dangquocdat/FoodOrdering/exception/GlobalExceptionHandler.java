    package com.dangquocdat.FoodOrdering.exception;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.context.request.WebRequest;

    import java.time.LocalDateTime;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(value = ApiException.class)
        public ResponseEntity<ErrorDetails> handleApiException(ApiException exception, WebRequest webRequest) {

            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false) // only get request URL, no other details
            );

            return ResponseEntity
                    .status(exception.getHttpStatus())
                    .body(errorDetails);
        }
    }
