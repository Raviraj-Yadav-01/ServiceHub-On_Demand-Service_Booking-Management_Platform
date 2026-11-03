package com.servicehub.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF disable kiya taaki POST requests block na hon
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Routing aur Authorization hum pehle hi RouteValidator aur Filter mein handle kar chuke hain
                );
        return http.build();
    }
}