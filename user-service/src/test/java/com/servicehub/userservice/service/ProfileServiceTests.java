package com.servicehub.userservice.service;

import com.servicehub.userservice.dto.request.ProfileUpdateRequest;
import com.servicehub.userservice.dto.response.ProfileResponse;
import com.servicehub.userservice.entity.UserProfile;
import com.servicehub.userservice.exception.ResourceNotFoundException;
import com.servicehub.userservice.mapper.ProfileMapper;
import com.servicehub.userservice.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    private UserProfile mockProfile;
    private ProfileResponse mockResponse;
    private final String AUTH_USER_ID = "user_101";

    @BeforeEach
    void setUp() {
        mockProfile = UserProfile.builder()
                .id(1L)
                .authUserId(AUTH_USER_ID)
                .firstName("Rahul")
                .lastName("Sharma")
                .phoneNumber("+919876543210")
                .build();

        mockResponse = ProfileResponse.builder()
                .id(1L)
                .authUserId(AUTH_USER_ID)
                .firstName("Rahul")
                .lastName("Sharma")
                .phoneNumber("+919876543210")
                .build();
    }

    @Test
    void getProfile_Success() {
        when(profileRepository.findByAuthUserId(AUTH_USER_ID)).thenReturn(Optional.of(mockProfile));
        when(profileMapper.toResponse(mockProfile)).thenReturn(mockResponse);

        ProfileResponse result = profileService.getProfile(AUTH_USER_ID);

        assertNotNull(result);
        assertEquals("Rahul", result.getFirstName());
        verify(profileRepository, times(1)).findByAuthUserId(AUTH_USER_ID);
    }

    @Test
    void getProfile_NotFound_ThrowsException() {
        when(profileRepository.findByAuthUserId(AUTH_USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileService.getProfile(AUTH_USER_ID));
        verify(profileRepository, times(1)).findByAuthUserId(AUTH_USER_ID);
    }

    @Test
    void updateProfile_Success() {
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFirstName("Rahul");
        request.setLastName("Verma");
        request.setPhoneNumber("+919876543210");

        when(profileRepository.findByAuthUserId(AUTH_USER_ID)).thenReturn(Optional.of(mockProfile));
        when(profileRepository.save(any(UserProfile.class))).thenReturn(mockProfile);
        when(profileMapper.toResponse(mockProfile)).thenReturn(mockResponse);

        ProfileResponse result = profileService.updateProfile(AUTH_USER_ID, request);

        assertNotNull(result);
        verify(profileRepository, times(1)).save(any(UserProfile.class));
    }
}