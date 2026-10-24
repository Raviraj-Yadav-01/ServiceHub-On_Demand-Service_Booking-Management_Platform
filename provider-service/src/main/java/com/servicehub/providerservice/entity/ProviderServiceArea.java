package com.servicehub.providerservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "provider_service_areas",
        indexes = {
                @Index(name = "idx_area_profile_id", columnList = "provider_profile_id")
        },
        uniqueConstraints = {
                // Ek provider ek hi pincode ko do baar add na kare
                @UniqueConstraint(columnNames = {"provider_profile_id", "pincode"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderServiceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    private ProviderProfile providerProfile;

    @Column(name = "pincode", nullable = false, length = 20)
    private String pincode;

    @Column(name = "city_name", nullable = false, length = 50)
    private String cityName;

    @Column(name = "area_name", length = 100)
    private String areaName; // e.g., "MP Nagar", "Kolar Road"

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}