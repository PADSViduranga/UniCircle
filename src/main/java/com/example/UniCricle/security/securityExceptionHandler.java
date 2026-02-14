package com.example.UniCricle.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class securityExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String,String>> handleLockedException(LockedException e){
        Map<String,String> response = new HashMap<>();

        response.put("error", "Account Locked");
        response.put("message", "Your account is locked for 3 hours due to 3 failed login attempts.");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
