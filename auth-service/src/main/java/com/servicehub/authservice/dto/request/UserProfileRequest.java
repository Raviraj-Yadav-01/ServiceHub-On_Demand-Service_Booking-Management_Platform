package com.servicehub.authservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileRequest {
    private Long authUserId; // Yeh sabse zaruri hai dono ko link karne ke liye
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}