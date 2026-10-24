package com.servicehub.providerservice.controller;

import com.servicehub.providerservice.dto.request.ServiceAreaRequest;
import com.servicehub.providerservice.dto.response.ServiceAreaResponse;
import com.servicehub.providerservice.service.ProviderServiceAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/providers/me/areas")
@RequiredArgsConstructor
public class ProviderServiceAreaController {

    private final ProviderServiceAreaService areaService;

    @PostMapping
    public ResponseEntity<ServiceAreaResponse> addArea(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @Valid @RequestBody ServiceAreaRequest request) {
        ServiceAreaResponse response = areaService.addServiceArea(authUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceAreaResponse>> getMyAreas(
            @RequestHeader("X-Auth-User-Id") String authUserId) {
        return ResponseEntity.ok(areaService.getServiceAreas(authUserId));
    }

    @DeleteMapping("/{areaId}")
    public ResponseEntity<Void> deleteArea(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @PathVariable Long areaId) {
        areaService.deleteServiceArea(authUserId, areaId);
        return ResponseEntity.ok().build();
    }
}