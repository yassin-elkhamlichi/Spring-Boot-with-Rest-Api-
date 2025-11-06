package com.codewithmosh.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
    private String secret;
    private int tokenRExpiration;// token well expires in 7d || token refresh expiration
    private int tokenAExpiration;/// token well expires in 5min

}
