package com.codewithmosh.store.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Max(value = 50, message = "Name must be less than 50 characters")
    private String name;
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size( min= 6,max = 12, message = "Password must be bettwen 6 and 12 characters")
    private String password;
}
