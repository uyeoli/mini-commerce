package com.config;

import com.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtProvider jwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration_time}") long expirationTime) {
        return new JwtProvider(secret, expirationTime);
    }
}