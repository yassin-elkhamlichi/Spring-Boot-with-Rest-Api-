package com.codewithmosh.store.config;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
    private String secret;
    private int TimeOutR;
    private int TimeOutA;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
