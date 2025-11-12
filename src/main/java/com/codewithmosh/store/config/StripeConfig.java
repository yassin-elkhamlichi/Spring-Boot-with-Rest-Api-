package com.codewithmosh.store.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Data
public class StripeConfig {
    @Value("${spring.stripe.secretKey}")
    private String Key;

    @PostConstruct
    public void init(){
        Stripe.apiKey = Key;
    }
}
