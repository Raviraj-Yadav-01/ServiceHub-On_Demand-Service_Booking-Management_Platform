package com.servicehub.userservice.controllers;

import com.servicehub.userservice.dto.request.AddressRequest;
import com.servicehub.userservice.dto.response.AddressResponse;
import com.servicehub.userservice.dto.response.ApiResponse;
import com.servicehub.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @Valid @RequestBody AddressRequest request) {
        AddressResponse response = addressService.addAddress(authUserId, request);
        return new ResponseEntity<>(ApiResponse.success("Address added successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getUserAddresses(
            @RequestHeader("X-Auth-User-Id") String authUserId) {
        List<AddressResponse> addresses = addressService.getUserAddresses(authUserId);
        return ResponseEntity.ok(ApiResponse.success("Addresses fetched successfully", addresses));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @PathVariable Long addressId) {
        AddressResponse address = addressService.getAddressById(authUserId, addressId);
        return ResponseEntity.ok(ApiResponse.success("Address fetched successfully", address));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        AddressResponse updated = addressService.updateAddress(authUserId, addressId, request);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully", updated));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @PathVariable Long addressId) {
        addressService.deleteAddress(authUserId, addressId);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @RequestHeader("X-Auth-User-Id") String authUserId,
            @PathVariable Long addressId) {
        AddressResponse defaultAddress = addressService.setDefaultAddress(authUserId, addressId);
        return ResponseEntity.ok(ApiResponse.success("Default address updated successfully", defaultAddress));
    }
}