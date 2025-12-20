package com.example.uniCircle.service;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public void validate(String password){
        if(password==null || password.trim().isEmpty()){
            throw new IllegalArgumentException("password shouldn't be empty");
        }
        if(password.length()<6){
            throw  new IllegalArgumentException("password should contain at least " +
                    "6 characters");
        }
        if(!password.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("password should contain at least " +
                    "one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")){
            throw new IllegalArgumentException("password should contain at least" +
                    "one lowercase letter");
        }
        if (!password.matches(".*\\d.*")){
            throw new IllegalArgumentException("password should contain at least " +
                    "one digit");
        }
        String[] commonPasswords={
                "password","password123","123456","abcdef"
        };
        String lowerPassword=password.toLowerCase();
        for (String common: commonPasswords){
            if(lowerPassword.contains(common)){
                throw new IllegalArgumentException("password is too weak");
            }
        }
    }
}
