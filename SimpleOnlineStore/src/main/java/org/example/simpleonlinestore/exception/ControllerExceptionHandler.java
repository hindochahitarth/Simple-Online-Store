package org.example.simpleonlinestore.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)

    public ErrorMessage expiredJwtToken(ExpiredJwtException e, WebRequest request){
        log.warn("Invalid JWT Token ");
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Your access token has expired. Please log in again to refresh token"+e.getMessage(),
                request.getDescription(false)

        );
    }
    @ExceptionHandler(JwtTokenMissingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage jwtTokenMissing(
            JwtTokenMissingException e,
            WebRequest request) {

        log.warn("JWT token missing");

        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Authorization token not found. Please provide a Bearer token.",
                request.getDescription(false)
        );
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage badCredentials(
            BadCredentialsException e,
            WebRequest request) {

        log.warn("Login failed: invalid username or password");

        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Invalid email or password.",
                request.getDescription(false)
        );
    }
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage invalidJwtToken(
            JwtException e,
            WebRequest request) {

        log.warn("Invalid JWT token: {}", e.getMessage());

        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Invalid or tampered access token. Please login again.",
                request.getDescription(false)
        );
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage userAlreadyExists(UserAlreadyExistsException e,WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );

    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException e,WebRequest request){
            ErrorMessage errorMessage=new ErrorMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    new Date(),
                    e.getMessage(),
                    request.getDescription(false)

            );
            return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, List<String>>> handleTypeMismatch(HttpMessageNotReadableException e) {
        String message = "Invalid input type provided.";

        // Simple trick to extract the field name from the exception message
        String details = e.getMostSpecificCause().getMessage();
        if (details != null && details.contains("[\"")) {
            String fieldName = details.substring(details.lastIndexOf("[\"") + 2, details.lastIndexOf("\"]"));
            message = "Invalid value provided for field: '" + fieldName + "'. Please enter a valid number.";
        }

        return new ResponseEntity<>(getErrorsMap(List.of(message)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> invalidUserDetails(MethodArgumentNotValidException e, WebRequest request){
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> {
                    // Check if it's a type mismatch conversion error (like letters in a number field)
                    if ("typeMismatch".equals(fieldError.getCode())) {
                        Object rejectedValue = fieldError.getRejectedValue();
                        return "Invalid value '" + rejectedValue + "' for field '" + fieldError.getField() + "'. Please enter a valid number.";
                    }
                    return fieldError.getDefaultMessage();
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String,List<String>> getErrorsMap(List<String> errors){
        Map<String,List<String>> errorResponse=new HashMap<>();
        errorResponse.put("errors",errors);
        return errorResponse;
    }



}
