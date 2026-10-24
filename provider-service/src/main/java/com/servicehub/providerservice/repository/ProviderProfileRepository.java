package com.servicehub.providerservice.repository;

import com.servicehub.providerservice.entity.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {
    Optional<ProviderProfile> findByAuthUserId(String authUserId);
    boolean existsByAuthUserId(String authUserId);
}