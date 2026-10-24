package com.servicehub.providerservice.service;

import com.servicehub.providerservice.dto.request.ProviderProfileRequest;
import com.servicehub.providerservice.dto.response.ProviderProfileResponse;
import com.servicehub.providerservice.entity.ProviderProfile;
import com.servicehub.providerservice.entity.VerificationStatus;
import com.servicehub.providerservice.repository.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderProfileService {

    private final ProviderProfileRepository profileRepository;

    @Transactional
    public ProviderProfileResponse createProfile(String authUserId, ProviderProfileRequest request) {
        // Business Rule: One provider profile per auth identity
        if (profileRepository.existsByAuthUserId(authUserId)) {
            throw new IllegalStateException("Provider profile already exists for this user.");
        }

        ProviderProfile profile = ProviderProfile.builder()
                .authUserId(authUserId)
                .businessName(request.getBusinessName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .bio(request.getBio())
                .experienceYears(request.getExperienceYears())
                .verificationStatus(VerificationStatus.PENDING) // Always PENDING on creation
                .build();

        ProviderProfile savedProfile = profileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public ProviderProfileResponse getProfile(String authUserId) {
        ProviderProfile profile = profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found."));
        return mapToResponse(profile);
    }

    private ProviderProfileResponse mapToResponse(ProviderProfile profile) {
        return ProviderProfileResponse.builder()
                .id(profile.getId())
                .businessName(profile.getBusinessName())
                .contactEmail(profile.getContactEmail())
                .contactPhone(profile.getContactPhone())
                .bio(profile.getBio())
                .experienceYears(profile.getExperienceYears())
                .verificationStatus(profile.getVerificationStatus().name())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}