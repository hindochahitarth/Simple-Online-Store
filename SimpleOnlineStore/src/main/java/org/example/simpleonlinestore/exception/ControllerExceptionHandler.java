package org.example.simpleonlinestore.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> invalidUserDetails(MethodArgumentNotValidException e, WebRequest request){
        List<String> errors=e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors),new HttpHeaders(),HttpStatus.BAD_REQUEST);
    }
    private Map<String,List<String>> getErrorsMap(List<String> errors){
        Map<String,List<String>> errorResponse=new HashMap<>();
        errorResponse.put("errors",errors);
        return errorResponse;
    }
}
