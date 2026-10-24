package com.servicehub.providerservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "provider_profiles",
        indexes = {
                @Index(name = "idx_provider_auth_id", columnList = "auth_user_id", unique = true)
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Maps to the auth-service user ID.
    @Column(name = "auth_user_id", nullable = false, unique = true, length = 100)
    private String authUserId;

    @Column(name = "business_name", nullable = false, length = 100)
    private String businessName;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false, length = 20)
    private String contactPhone;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    @Builder.Default
    private com.servicehub.providerservice.entity.VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}