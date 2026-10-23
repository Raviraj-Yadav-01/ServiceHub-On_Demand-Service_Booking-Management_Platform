package com.servicehub.userservice.mapper;

import com.servicehub.userservice.dto.response.ProfileResponse;
import com.servicehub.userservice.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponse toResponse(UserProfile profile) {
        if (profile == null) return null;

        return ProfileResponse.builder()
                .id(profile.getId())
                .authUserId(profile.getAuthUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .profileImageUrl(profile.getProfileImageUrl())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}