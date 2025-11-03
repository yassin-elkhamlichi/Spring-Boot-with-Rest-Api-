package com.codewithmosh.store.dtos;


import com.codewithmosh.store.validation.LowerCase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(min=  2, max = 50, message = "Name must be between 2 and 50 characters")
    @LowerCase
    private String name;
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size( min= 6,max = 12, message = "Password must be bettwen 6 and 12 characters")
    private String password;
}
