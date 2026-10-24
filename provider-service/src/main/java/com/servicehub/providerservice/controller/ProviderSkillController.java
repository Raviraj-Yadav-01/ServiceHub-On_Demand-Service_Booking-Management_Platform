package com.servicehub.providerservice.controller;

import com.servicehub.providerservice.dto.request.SkillRequest;
import com.servicehub.providerservice.dto.response.SkillResponse;
import com.servicehub.providerservice.service.ProviderSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/providers/me/skills")
@RequiredArgsConstructor
public class ProviderSkillController {

    private final ProviderSkillService skillService;

    @PostMapping
    public ResponseEntity<SkillResponse> addSkill(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @Valid @RequestBody SkillRequest request) {
        SkillResponse response = skillService.addSkill(authUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getMySkills(
            @RequestHeader("X-Auth-User-Id") String authUserId) {
        List<SkillResponse> response = skillService.getProviderSkills(authUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @PathVariable Long skillId) {
        skillService.deleteSkill(authUserId, skillId);
        return ResponseEntity.ok().build();
    }
}