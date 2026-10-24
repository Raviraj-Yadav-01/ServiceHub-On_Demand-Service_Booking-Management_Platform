package com.servicehub.providerservice.controller;

import com.servicehub.providerservice.dto.request.ProviderProfileRequest;
import com.servicehub.providerservice.dto.response.ProviderProfileResponse;
import com.servicehub.providerservice.service.ProviderProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/providers/me")
@RequiredArgsConstructor
public class ProviderProfileController {

    private final ProviderProfileService profileService;

    @PostMapping
    public ResponseEntity<ProviderProfileResponse> onboardProvider(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @Valid @RequestBody ProviderProfileRequest request) {
        ProviderProfileResponse response = profileService.createProfile(authUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ProviderProfileResponse> getMyProfile(
            @RequestHeader("X-Auth-User-Id") String authUserId) {
        ProviderProfileResponse response = profileService.getProfile(authUserId);
        return ResponseEntity.ok(response);
    }
}