package com.servicehub.authservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private Long authUserId;
    private String email;
    private String role;
}