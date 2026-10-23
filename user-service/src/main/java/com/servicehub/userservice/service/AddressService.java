package com.servicehub.userservice.service;

import com.servicehub.userservice.dto.request.AddressRequest;
import com.servicehub.userservice.dto.response.AddressResponse;
import com.servicehub.userservice.entity.UserAddress;
import com.servicehub.userservice.entity.UserProfile;
import com.servicehub.userservice.exception.ResourceNotFoundException;
import com.servicehub.userservice.exception.UnauthorizedAccessException;
import com.servicehub.userservice.mapper.AddressMapper;
import com.servicehub.userservice.repository.UserAddressRepository;
import com.servicehub.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final UserAddressRepository addressRepository;
    private final UserProfileRepository profileRepository;
    private final AddressMapper addressMapper;

    @Transactional
    public AddressResponse addAddress(String authUserId, AddressRequest request) {
        UserProfile profile = getProfileByAuthUserId(authUserId);
        List<UserAddress> existingAddresses = addressRepository.findByUserProfileId(profile.getId());

        UserAddress address = addressMapper.toEntity(request);
        address.setUserProfile(profile);

        // First address is automatically default, or reset old default if new address is specified as default
        if (existingAddresses.isEmpty() || Boolean.TRUE.equals(request.getIsDefault())) {
            unsetExistingDefaultAddress(profile.getId());
            address.setIsDefault(true);
        }

        UserAddress savedAddress = addressRepository.save(address);
        return addressMapper.toResponse(savedAddress);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(String authUserId) {
        UserProfile profile = getProfileByAuthUserId(authUserId);
        return addressRepository.findByUserProfileId(profile.getId()).stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse getAddressById(String authUserId, Long addressId) {
        UserProfile profile = getProfileByAuthUserId(authUserId);
        UserAddress address = getOwnedAddress(addressId, profile.getId());
        return addressMapper.toResponse(address);
    }

    @Transactional
    public AddressResponse updateAddress(String authUserId, Long addressId, AddressRequest request) {
        UserProfile profile = getProfileByAuthUserId(authUserId);
        UserAddress address = getOwnedAddress(addressId, profile.getId());

        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(address.getIsDefault())) {
            unsetExistingDefaultAddress(profile.getId());
            address.setIsDefault(true);
        }

        address.setAddressLabel(request.getAddressLabel());
        address.setRecipientName(request.getRecipientName());
        address.setRecipientPhone(request.getRecipientPhone());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(String authUserId, Long addressId) {
        UserProfile profile = getProfileByAuthUserId(authUserId);
        UserAddress address = getOwnedAddress(addressId, profile.getId());

        boolean wasDefault = Boolean.TRUE.equals(address.getIsDefault());
        addressRepository.delete(address);

        // If the deleted address was default, promote another remaining address as default
        if (wasDefault) {
            List<UserAddress> remaining = addressRepository.findByUserProfileId(profile.getId());
            if (!remaining.isEmpty()) {
                UserAddress newDefault = remaining.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }
    }

    @Transactional
    public AddressResponse setDefaultAddress(String authUserId, Long addressId) {
        UserProfile profile = getProfileByAuthUserId(authUserId);
        UserAddress address = getOwnedAddress(addressId, profile.getId());

        unsetExistingDefaultAddress(profile.getId());
        address.setIsDefault(true);

        return addressMapper.toResponse(addressRepository.save(address));
    }

    private UserProfile getProfileByAuthUserId(String authUserId) {
        return profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found. Please update profile first."));
    }

    private UserAddress getOwnedAddress(Long addressId, Long profileId) {
        return addressRepository.findByIdAndUserProfileId(addressId, profileId)
                .orElseThrow(() -> new UnauthorizedAccessException("Address not found or access denied for address ID: " + addressId));
    }

    private void unsetExistingDefaultAddress(Long profileId) {
        addressRepository.findByUserProfileIdAndIsDefaultTrue(profileId)
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    addressRepository.save(existingDefault);
                });
    }
}