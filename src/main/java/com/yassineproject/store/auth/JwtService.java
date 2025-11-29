package com.yassineproject.store.auth;

import com.yassineproject.store.users.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getTimeOutA());
    }

    public Jwt generateRefershToken(User user) {
        return generateToken(user, jwtConfig.getTimeOutR());
    }

    public Jwt generateToken(User user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(Long.toString(user.getId()))
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("Role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();
        return new Jwt(claims, (jwtConfig.getSecretKey()));
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Jwt parse(String token) {
        try {
            Claims claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }

    }

}
