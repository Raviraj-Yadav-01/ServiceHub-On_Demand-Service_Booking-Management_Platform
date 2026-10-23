package com.servicehub.userservice.service;

import com.servicehub.userservice.dto.request.ProfileUpdateRequest;
import com.servicehub.userservice.dto.request.UserProfileCreateRequest;
import com.servicehub.userservice.dto.response.ProfileResponse;
import com.servicehub.userservice.entity.UserProfile;
import com.servicehub.userservice.exception.ResourceNotFoundException;
import com.servicehub.userservice.mapper.ProfileMapper;
import com.servicehub.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String authUserId) {
        UserProfile profile = profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + authUserId));
        return profileMapper.toResponse(profile);
    }


    @Transactional
    public ProfileResponse updateProfile(String authUserId, ProfileUpdateRequest request) {
        UserProfile profile = profileRepository.findByAuthUserId(authUserId)
                .orElseGet(() -> UserProfile.builder()
                        .authUserId(authUserId)
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .build());

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setProfileImageUrl(request.getProfileImageUrl());

        UserProfile updatedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(updatedProfile);
    }


    public void createProfile(UserProfileCreateRequest request) {

        UserProfile newProfile = UserProfile.builder()
                .authUserId(request.getAuthUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        profileRepository.save(newProfile);
    }
}