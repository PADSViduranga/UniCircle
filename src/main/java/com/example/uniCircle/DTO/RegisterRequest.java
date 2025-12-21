package com.example.uniCircle.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    public RegisterRequest(){

    }
    public RegisterRequest(String fullName,String email,String password){
        this.fullName=fullName;
        this.email=email;
        this.password=password;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName=fullName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPassword(){
        return password=password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object object){
        if (this==object) return true;
        if (object==null || getClass() !=object.getClass())
            return false;

        RegisterRequest request=(RegisterRequest) object;

        if (!fullName.equals(request.fullName))
            return false;
        if (!email.equals(request.email))
            return false;

        return password.equals(request.password);
    }
    @Override
    public int hashCode(){
        int result=fullName.hashCode();
        result=31*result+email.hashCode();
        result=31*result+password.hashCode();

        return result;
    }


}
