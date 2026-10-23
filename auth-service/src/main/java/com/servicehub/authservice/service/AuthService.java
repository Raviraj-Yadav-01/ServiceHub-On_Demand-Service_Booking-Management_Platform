package com.servicehub.authservice.service;

import com.servicehub.authservice.client.UserServiceClient;
import com.servicehub.authservice.dto.request.LoginRequest;
import com.servicehub.authservice.dto.request.RegisterRequest;
import com.servicehub.authservice.dto.request.UserProfileRequest;
import com.servicehub.authservice.dto.response.AuthResponse;
import com.servicehub.authservice.entity.AuthUser;
import com.servicehub.authservice.enums.Role;
import com.servicehub.authservice.repository.AuthUserRepository;
import com.servicehub.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Check if email already exists
        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        // 2. Create new user entity and encrypt password
        AuthUser user = AuthUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER) // Default role assigning
                .isEnabled(true)
                .build();

        // 3. Save to database
        AuthUser savedUser = authUserRepository.save(user);

        // ==========================================
        // 4. INTER-SERVICE COMMUNICATION: User Profile creation
        // User-service (port 8081) ko baaki ki details bhejna
        // ==========================================
        UserProfileRequest profileRequest = UserProfileRequest.builder()
                .authUserId(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        // Yeh method background mein HTTP request bhejega user-service ko
        userServiceClient.createProfileInUserService(profileRequest);

        // 5. Generate JWT Token
        String jwtToken = generateTokenWithExtraClaims(savedUser);

        // 6. Return Response
        return AuthResponse.builder()
                .token(jwtToken)
                .authUserId(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Authenticate user (This will automatically check password using PasswordEncoder)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Fetch authenticated user from DB
        AuthUser user = authUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. Generate JWT Token
        String jwtToken = generateTokenWithExtraClaims(user);

        // 4. Return Response
        return AuthResponse.builder()
                .token(jwtToken)
                .authUserId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    private String generateTokenWithExtraClaims(AuthUser user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());  // Bahut zaruri: API gateway yahi se id nikalega
        extraClaims.put("role", user.getRole().name());

        return jwtService.generateToken(extraClaims, user);
    }
}