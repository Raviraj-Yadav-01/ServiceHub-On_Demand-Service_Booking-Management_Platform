package com.servicehub.providerservice.service;

import com.servicehub.providerservice.dto.request.SkillRequest;
import com.servicehub.providerservice.dto.response.SkillResponse;
import com.servicehub.providerservice.entity.ProviderProfile;
import com.servicehub.providerservice.entity.ProviderSkill;
import com.servicehub.providerservice.repository.ProviderProfileRepository;
import com.servicehub.providerservice.repository.ProviderSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderSkillService {

    private final ProviderSkillRepository skillRepository;
    private final ProviderProfileRepository profileRepository;

    private ProviderProfile getVerifiedProfile(String authUserId) {
        return profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found. Please create a profile first."));
    }

    @Transactional
    public SkillResponse addSkill(String authUserId, SkillRequest request) {
        ProviderProfile profile = getVerifiedProfile(authUserId);

        ProviderSkill skill = ProviderSkill.builder()
                .providerProfile(profile)
                .category(request.getCategory())
                .skillName(request.getSkillName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        ProviderSkill savedSkill = skillRepository.save(skill);
        return mapToResponse(savedSkill);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getProviderSkills(String authUserId) {
        ProviderProfile profile = getVerifiedProfile(authUserId);
        return skillRepository.findByProviderProfileId(profile.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSkill(String authUserId, Long skillId) {
        ProviderProfile profile = getVerifiedProfile(authUserId);

        // Security check: Ensure skill belongs to this provider
        ProviderSkill skill = skillRepository.findByIdAndProviderProfileId(skillId, profile.getId())
                .orElseThrow(() -> new RuntimeException("Skill not found or unauthorized access."));

        skillRepository.delete(skill);
    }

    private SkillResponse mapToResponse(ProviderSkill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .category(skill.getCategory())
                .skillName(skill.getSkillName())
                .description(skill.getDescription())
                .price(skill.getPrice())
                .createdAt(skill.getCreatedAt())
                .build();
    }
}