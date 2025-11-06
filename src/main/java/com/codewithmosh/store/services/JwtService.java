package com.codewithmosh.store.services;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.dtos.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateAccessToken(UserDto userDto) {
        return generateToken(userDto, jwtConfig.getTokenAExpiration());
    }

    public String generateRefershToken(UserDto userDto) {

        return generateToken(userDto, jwtConfig.getTokenRExpiration());

    }

    private String generateToken(UserDto userDto, long tokenExpiration) {
        return Jwts.builder()
                .subject(Long.toString(userDto.getId()))
                .claim("email", userDto.getEmail())
                .claim("name", userDto.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getPayload(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
    }

    public Long getId(String token) {
        var claims = getPayload(token);
        String subject = claims.getSubject();
        // defensive checks: subject may be null, empty or the literal string "null"
        if (subject == null || subject.isEmpty() || "null".equalsIgnoreCase(subject.trim())) {
            throw new JwtException("Invalid token subject");
        }

        try {
            return Long.parseLong(subject.trim());
        } catch (NumberFormatException ex) {
            throw new JwtException("Invalid token subject: not a valid id", ex);
        }
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
