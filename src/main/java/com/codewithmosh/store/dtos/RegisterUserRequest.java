package com.codewithmosh.store.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
