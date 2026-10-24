package com.servicehub.providerservice.service;

import com.servicehub.providerservice.dto.request.AvailabilityRequest;
import com.servicehub.providerservice.dto.response.AvailabilityResponse;
import com.servicehub.providerservice.entity.ProviderAvailability;
import com.servicehub.providerservice.entity.ProviderProfile;
import com.servicehub.providerservice.repository.ProviderAvailabilityRepository;
import com.servicehub.providerservice.repository.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderAvailabilityService {

    private final ProviderAvailabilityRepository availabilityRepository;
    private final ProviderProfileRepository profileRepository;

    private ProviderProfile getVerifiedProfile(String authUserId) {
        return profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found."));
    }

    @Transactional
    public AvailabilityResponse setAvailability(String authUserId, AvailabilityRequest request) {
        ProviderProfile profile = getVerifiedProfile(authUserId);

        // Validation: If available, start and end times must be provided and start < end
        if (Boolean.TRUE.equals(request.getIsAvailable())) {
            if (request.getStartTime() == null || request.getEndTime() == null) {
                throw new IllegalArgumentException("Start time and end time are required when available.");
            }
            if (request.getStartTime().isAfter(request.getEndTime())) {
                throw new IllegalArgumentException("Start time must be before end time.");
            }
        }

        // Upsert Logic (Agar record exist karta hai to update karo, warna create)
        ProviderAvailability availability = availabilityRepository
                .findByProviderProfileIdAndDayOfWeek(profile.getId(), request.getDayOfWeek())
                .orElse(ProviderAvailability.builder()
                        .providerProfile(profile)
                        .dayOfWeek(request.getDayOfWeek())
                        .build());

        availability.setIsAvailable(request.getIsAvailable());
        if (Boolean.TRUE.equals(request.getIsAvailable())) {
            availability.setStartTime(request.getStartTime());
            availability.setEndTime(request.getEndTime());
        } else {
            availability.setStartTime(null);
            availability.setEndTime(null);
        }

        ProviderAvailability saved = availabilityRepository.save(availability);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getMyAvailability(String authUserId) {
        ProviderProfile profile = getVerifiedProfile(authUserId);
        return availabilityRepository.findByProviderProfileId(profile.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AvailabilityResponse mapToResponse(ProviderAvailability availability) {
        return AvailabilityResponse.builder()
                .id(availability.getId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .isAvailable(availability.getIsAvailable())
                .build();
    }
}