package com.teamvoy.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseBody
    @ExceptionHandler(JwtAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return Map.of("errors", map);
    }

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadCredentialsException(BadCredentialsException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
        return Map.of("error", ex.getMessage());
    }
}
