package com.servicehub.apigateway.filter;

import com.servicehub.apigateway.config.RouteValidator;
import com.servicehub.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. Check if the route requires authentication
        if (validator.isSecured.test(exchange.getRequest())) {

            // 2. Check if Authorization header is present
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7); // Remove "Bearer " prefix
            }

            try {
                // 1. Token validate karein
                jwtUtil.validateToken(authHeader);

                // 2. Token se saara payload (claims) nikalein
                io.jsonwebtoken.Claims claims = jwtUtil.extractAllClaims(authHeader);

                // 3. Payload se "userId" nikal kar String mein convert karein
                String authUserId = String.valueOf(claims.get("userId"));
                String authRole = String.valueOf(claims.get("role"));

                // 4. Naya header "X-Auth-User-Id" add karein
                org.springframework.http.server.reactive.ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("X-Auth-User-Id", authUserId)
                        .header("X-Auth-User-Role", authRole)
                        .build();

                // 5. Exchange ko update karein
                exchange = exchange.mutate().request(request).build();

            } catch (Exception e) {
                return onError(exchange, "Unauthorized access to application", HttpStatus.UNAUTHORIZED);
            }
        }

        // 6. Forward request to destination service
        return chain.filter(exchange);
    } // <-- Aapka yeh wala bracket miss ho gaya tha shayad!

    // Helper method for errors
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Highest priority
    }
}