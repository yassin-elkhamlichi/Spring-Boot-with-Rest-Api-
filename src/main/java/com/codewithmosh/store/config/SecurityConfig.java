package com.codewithmosh.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //first we need to make session stateless
        //second Disable csrf
        //third disable default login page
         http
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(c -> c
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(c -> c
                            .requestMatchers("/carts/**").permitAll()
                            .requestMatchers("/swagger-ui").permitAll()
                            .anyRequest().authenticated());
            return http.build();
    }
}
