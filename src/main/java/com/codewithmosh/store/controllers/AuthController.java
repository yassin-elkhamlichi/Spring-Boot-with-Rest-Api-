package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AuthUserDto;
import com.codewithmosh.store.exception.InvalidPasswordException;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<String> auth(
            @Valid @RequestBody AuthUserDto authUserDto
    ) {
        String response = userService.authUser(authUserDto, userRepository);
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleUserNotFoundException(
            UserNotFoundException ex
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("message", "user not found")
        );
    }
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String,String>> handleInvalidPasswordException(
            InvalidPasswordException ex
    ){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}