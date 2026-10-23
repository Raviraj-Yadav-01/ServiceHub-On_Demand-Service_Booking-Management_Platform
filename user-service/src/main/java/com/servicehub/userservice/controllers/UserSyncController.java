package com.servicehub.userservice.controllers;

import com.servicehub.userservice.dto.request.UserProfileCreateRequest;
import com.servicehub.userservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserSyncController {

    private final ProfileService profileService;

    // Yeh wahi exact URL hai jahan auth-service data bhej raha hai
    @PostMapping("/profile")
    public ResponseEntity<String> syncNewUser(@RequestBody UserProfileCreateRequest request) {
        // Profile create karne ka logic call karein
        profileService.createProfile(request);
        return ResponseEntity.ok("Profile created successfully in user-service");
    }
}