package com.servicehub.userservice.mapper;

import com.servicehub.userservice.dto.request.AddressRequest;
import com.servicehub.userservice.dto.response.AddressResponse;
import com.servicehub.userservice.entity.UserAddress;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public UserAddress toEntity(AddressRequest request) {
        if (request == null) return null;

        return UserAddress.builder()
                .addressLabel(request.getAddressLabel())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .build();
    }

    public AddressResponse toResponse(UserAddress address) {
        if (address == null) return null;

        return AddressResponse.builder()
                .id(address.getId())
                .addressLabel(address.getAddressLabel())
                .recipientName(address.getRecipientName())
                .recipientPhone(address.getRecipientPhone())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}