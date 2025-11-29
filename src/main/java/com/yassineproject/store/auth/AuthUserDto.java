package com.yassineproject.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthUserDto {
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
