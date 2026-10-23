package com.servicehub.userservice.service;

import com.servicehub.userservice.dto.request.AddressRequest;
import com.servicehub.userservice.dto.response.AddressResponse;
import com.servicehub.userservice.entity.UserAddress;
import com.servicehub.userservice.entity.UserProfile;
import com.servicehub.userservice.exception.UnauthorizedAccessException;
import com.servicehub.userservice.mapper.AddressMapper;
import com.servicehub.userservice.repository.UserAddressRepository;
import com.servicehub.userservice.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private UserAddressRepository addressRepository;

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    private UserProfile mockProfile;
    private UserAddress mockAddress;
    private AddressResponse mockResponse;
    private final String AUTH_USER_ID = "user_101";

    @BeforeEach
    void setUp() {
        mockProfile = UserProfile.builder()
                .id(1L)
                .authUserId(AUTH_USER_ID)
                .firstName("Rahul")
                .lastName("Sharma")
                .build();

        mockAddress = UserAddress.builder()
                .id(10L)
                .userProfile(mockProfile)
                .addressLabel("Home")
                .addressLine1("Green Towers")
                .city("Bhopal")
                .state("MP")
                .postalCode("462001")
                .country("India")
                .isDefault(true)
                .build();

        mockResponse = AddressResponse.builder()
                .id(10L)
                .addressLabel("Home")
                .addressLine1("Green Towers")
                .city("Bhopal")
                .isDefault(true)
                .build();
    }

    @Test
    void addFirstAddress_AutoSetsDefaultTrue() {
        AddressRequest request = new AddressRequest();
        request.setAddressLabel("Home");
        request.setAddressLine1("Green Towers");
        request.setCity("Bhopal");
        request.setState("MP");
        request.setPostalCode("462001");
        request.setCountry("India");
        request.setIsDefault(false);

        when(profileRepository.findByAuthUserId(AUTH_USER_ID)).thenReturn(Optional.of(mockProfile));
        when(addressRepository.findByUserProfileId(1L)).thenReturn(Collections.emptyList());
        when(addressMapper.toEntity(request)).thenReturn(mockAddress);
        when(addressRepository.save(any(UserAddress.class))).thenReturn(mockAddress);
        when(addressMapper.toResponse(mockAddress)).thenReturn(mockResponse);

        AddressResponse response = addressService.addAddress(AUTH_USER_ID, request);

        assertNotNull(response);
        assertTrue(mockAddress.getIsDefault());
        verify(addressRepository, times(1)).save(mockAddress);
    }

    @Test
    void getAddressById_UnownedAddress_ThrowsUnauthorizedAccessException() {
        when(profileRepository.findByAuthUserId(AUTH_USER_ID)).thenReturn(Optional.of(mockProfile));
        when(addressRepository.findByIdAndUserProfileId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedAccessException.class, () -> addressService.getAddressById(AUTH_USER_ID, 99L));
    }

    @Test
    void deleteDefaultAddress_PromotesRemainingAddressToDefault() {
        UserAddress secondAddress = UserAddress.builder()
                .id(11L)
                .userProfile(mockProfile)
                .addressLabel("Work")
                .isDefault(false)
                .build();

        when(profileRepository.findByAuthUserId(AUTH_USER_ID)).thenReturn(Optional.of(mockProfile));
        when(addressRepository.findByIdAndUserProfileId(10L, 1L)).thenReturn(Optional.of(mockAddress));
        when(addressRepository.findByUserProfileId(1L)).thenReturn(List.of(secondAddress));

        addressService.deleteAddress(AUTH_USER_ID, 10L);

        verify(addressRepository, times(1)).delete(mockAddress);
        assertTrue(secondAddress.getIsDefault());
        verify(addressRepository, times(1)).save(secondAddress);
    }
}