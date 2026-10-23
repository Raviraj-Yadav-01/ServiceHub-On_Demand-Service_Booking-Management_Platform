package com.servicehub.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProfileResponse {
    private Long id;
    private String authUserId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}