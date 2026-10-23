package com.servicehub.userservice.controllers;

import com.servicehub.userservice.dto.request.ProfileUpdateRequest;
import com.servicehub.userservice.dto.response.ApiResponse;
import com.servicehub.userservice.dto.response.ProfileResponse;
import com.servicehub.userservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
            @RequestHeader("X-Auth-User-Id") String authUserId) {
        ProfileResponse profile = profileService.getProfile(authUserId);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @Valid @RequestBody ProfileUpdateRequest request) {
        ProfileResponse profile = profileService.updateProfile(authUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }
}