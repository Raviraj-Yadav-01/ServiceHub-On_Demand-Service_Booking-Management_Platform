package com.servicehub.providerservice.service;

import com.servicehub.providerservice.dto.request.ServiceAreaRequest;
import com.servicehub.providerservice.dto.response.ServiceAreaResponse;
import com.servicehub.providerservice.entity.ProviderProfile;
import com.servicehub.providerservice.entity.ProviderServiceArea;
import com.servicehub.providerservice.repository.ProviderProfileRepository;
import com.servicehub.providerservice.repository.ProviderServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderServiceAreaService {

    private final ProviderServiceAreaRepository areaRepository;
    private final ProviderProfileRepository profileRepository;

    private ProviderProfile getVerifiedProfile(String authUserId) {
        return profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found."));
    }

    @Transactional
    public ServiceAreaResponse addServiceArea(String authUserId, ServiceAreaRequest request) {
        ProviderProfile profile = getVerifiedProfile(authUserId);

        // Rule: Prevent duplicate pincodes for the same provider
        if (areaRepository.existsByProviderProfileIdAndPincode(profile.getId(), request.getPincode())) {
            throw new IllegalStateException("This pincode is already added to your service areas.");
        }

        ProviderServiceArea area = ProviderServiceArea.builder()
                .providerProfile(profile)
                .pincode(request.getPincode())
                .cityName(request.getCityName())
                .areaName(request.getAreaName())
                .build();

        ProviderServiceArea savedArea = areaRepository.save(area);
        return mapToResponse(savedArea);
    }

    @Transactional(readOnly = true)
    public List<ServiceAreaResponse> getServiceAreas(String authUserId) {
        ProviderProfile profile = getVerifiedProfile(authUserId);
        return areaRepository.findByProviderProfileId(profile.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteServiceArea(String authUserId, Long areaId) {
        ProviderProfile profile = getVerifiedProfile(authUserId);

        // Ownership Check
        ProviderServiceArea area = areaRepository.findByIdAndProviderProfileId(areaId, profile.getId())
                .orElseThrow(() -> new RuntimeException("Service area not found or unauthorized access."));

        areaRepository.delete(area);
    }

    private ServiceAreaResponse mapToResponse(ProviderServiceArea area) {
        return ServiceAreaResponse.builder()
                .id(area.getId())
                .pincode(area.getPincode())
                .cityName(area.getCityName())
                .areaName(area.getAreaName())
                .createdAt(area.getCreatedAt())
                .build();
    }
}