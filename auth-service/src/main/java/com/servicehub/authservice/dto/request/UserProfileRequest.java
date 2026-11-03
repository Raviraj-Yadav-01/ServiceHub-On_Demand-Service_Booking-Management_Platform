package com.servicehub.authservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileRequest {
    private Long authUserId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}