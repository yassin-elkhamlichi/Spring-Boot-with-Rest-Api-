package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    final long tokenExpiration = 86400; // token well expires in 24 hours
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateToken(UserDto userDto){
        return Jwts.builder()
            .subject(Long.toString(userDto.getId()))
            .claim("email" , userDto.getEmail())
            .claim("name" , userDto.getName())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();

    }

    public boolean validateToken(String token){
        try{
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
        if(subject == null || subject.isEmpty() || "null".equalsIgnoreCase(subject.trim())){
            throw new JwtException("Invalid token subject");
        }

        try{
            return Long.parseLong(subject.trim());
        } catch (NumberFormatException ex){
            throw new JwtException("Invalid token subject: not a valid id", ex);
        }
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
