package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AuthUserDto;
import com.codewithmosh.store.dtos.JwtResponseDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exception.InvalidPasswordException;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @PostMapping("login")
    public ResponseEntity<JwtResponseDto> auth(
            @Valid @RequestBody AuthUserDto authUserDto
    ) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       authUserDto.getEmail(),
                       authUserDto.getPassword()
               )
       );
        var user = userMapper.toDto(userRepository.findByEmail(authUserDto.getEmail()).orElseThrow());
        if (user == null) {
            throw new UserNotFoundException();
        }
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (Long)  authentication.getPrincipal();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    @PostMapping("validate")
    public boolean validate(
            @RequestHeader("Authorization") String token
    ){
        System.out.println("Validate Called with header");
        var tokenWithoutBearer = token.replace("Bearer ","");
        return jwtService.validateToken(tokenWithoutBearer);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}