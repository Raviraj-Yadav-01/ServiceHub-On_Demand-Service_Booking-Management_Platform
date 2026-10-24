package com.servicehub.providerservice.controller;

import com.servicehub.providerservice.dto.request.AvailabilityRequest;
import com.servicehub.providerservice.dto.response.AvailabilityResponse;
import com.servicehub.providerservice.service.ProviderAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/providers/me/availability")
@RequiredArgsConstructor
public class ProviderAvailabilityController {

    private final ProviderAvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<AvailabilityResponse> setAvailability(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @Valid @RequestBody AvailabilityRequest request) {
        return ResponseEntity.ok(availabilityService.setAvailability(authUserId, request));
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponse>> getMyAvailability(
            @RequestHeader("X-Auth-User-Id") String authUserId) {
        return ResponseEntity.ok(availabilityService.getMyAvailability(authUserId));
    }
}