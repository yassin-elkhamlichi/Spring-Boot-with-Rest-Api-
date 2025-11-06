package com.codewithmosh.store.controllers;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.dtos.AuthUserDto;
import com.codewithmosh.store.dtos.JwtResponseDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("login")
    public ResponseEntity<JwtResponseDto> auth(
            @Valid @RequestBody AuthUserDto authUserDto,
            HttpServletResponse response

    ) {

        var user = userMapper.toDto(userRepository.findByEmail(authUserDto.getEmail()).orElse(null));
        if (user == null) {
            throw new UserNotFoundException();
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUserDto.getEmail(),
                        authUserDto.getPassword()));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefershToken(user);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setSecure(false);
        cookie.setMaxAge(jwtConfig.getTimeOutR()); // 7d
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponseDto(accessToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("####################" + authentication.getPrincipal());
        var id = (Long) authentication.getPrincipal();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "refreshToken") String refreshToken) {
        if (!jwtService.validateToken(refreshToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", "the token is invalid"));
        var userId = jwtService.getId(refreshToken);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(userMapper.toDto(user));
        return ResponseEntity.ok(new JwtResponseDto(accessToken));

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error", "Invalid password"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error", "Invalid email"));
    }
}