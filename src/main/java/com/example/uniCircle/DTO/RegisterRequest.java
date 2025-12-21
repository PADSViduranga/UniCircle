package com.example.uniCircle.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jdk.jfr.DataAmount;

@DataAmount
public class RegisterRequest {
    @NotBlank(message = "required full name ")
    @Size(min = 3, max = 30,message = "full name should be between 3 and " +
            "30 characters")
    private String fullName;

    @NotBlank(message = "your student email is required")
    @Email(message = "email should be valid")
    @Pattern(regexp = ".+@.+\\.ac\\.lk$",message = "email domain should be ac.lk")
    private String email;

    @NotBlank(message = "you need a password")
    @Size(min = 6, message = "password should contain  at least 6 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d).*$",
            message ="password should contain at leat one uppercase and one lowercase" +
                    "letter" )
    private String password;
}
