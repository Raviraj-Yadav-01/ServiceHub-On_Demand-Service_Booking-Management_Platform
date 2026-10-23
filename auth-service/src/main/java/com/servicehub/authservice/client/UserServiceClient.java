package com.servicehub.authservice.client;

import com.servicehub.authservice.dto.request.UserProfileRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient() {
        // user-service ka base URL port 8081 par set kar rahe hain
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    public void createProfileInUserService(UserProfileRequest request) {
        try {
            restClient.post()
                    .uri("/api/v1/users/profile") // user-service ka endpoint
                    .body(request)
                    .retrieve()
                    .toBodilessEntity(); // Humein bas success status chahiye, response body nahi

            System.out.println("✅ Profile successfully created in user-service for Auth ID: " + request.getAuthUserId());
        } catch (Exception e) {
            System.err.println("❌ Failed to create profile in user-service. Error: " + e.getMessage());
            // Real production mein hum yahan retry logic ya RabbitMQ/Kafka use karte hain
        }
    }
}