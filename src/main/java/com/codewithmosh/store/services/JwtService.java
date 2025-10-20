package com.codewithmosh.store.services;

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

    public String generateToken(String email){
    return Jwts.builder()
            .subject(email)
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
    public String getEmail(String token){
        var claims = getPayload(token);
        return claims.getSubject();
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
