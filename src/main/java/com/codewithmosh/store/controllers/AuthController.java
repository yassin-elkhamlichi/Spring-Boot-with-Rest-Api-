package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AuthUserDto;
import com.codewithmosh.store.exception.InvalidPasswordException;
import com.codewithmosh.store.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public ResponseEntity<Void> auth(
            @Valid @RequestBody AuthUserDto authUserDto
    ) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       authUserDto.getEmail(),
                       authUserDto.getPassword()
               )
       );
        return ResponseEntity.ok().build();
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