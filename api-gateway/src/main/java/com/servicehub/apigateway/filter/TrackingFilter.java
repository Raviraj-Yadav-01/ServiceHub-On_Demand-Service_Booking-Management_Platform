package com.servicehub.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TrackingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);

        // Agar header mein ID nahi hai, toh ek nayi generate karke daal do

        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
            exchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header(CORRELATION_ID_HEADER, correlationId)
                            .build())
                    .build();
        }

        logger.info("ServiceHub Request Tracked - Correlation ID: {}", correlationId);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Sabse pehle execute hone ke liye
    }
}