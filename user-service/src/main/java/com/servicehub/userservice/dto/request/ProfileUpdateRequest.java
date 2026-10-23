package com.servicehub.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfileUpdateRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Pattern(regexp = "^$|^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String profileImageUrl;
}