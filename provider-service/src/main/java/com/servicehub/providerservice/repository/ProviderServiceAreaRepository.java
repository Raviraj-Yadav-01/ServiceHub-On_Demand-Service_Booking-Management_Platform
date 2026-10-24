package com.servicehub.providerservice.repository;

import com.servicehub.providerservice.entity.ProviderServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderServiceAreaRepository extends JpaRepository<ProviderServiceArea, Long> {
    List<ProviderServiceArea> findByProviderProfileId(Long providerProfileId);
    Optional<ProviderServiceArea> findByIdAndProviderProfileId(Long id, Long providerProfileId);
    boolean existsByProviderProfileIdAndPincode(Long providerProfileId, String pincode);
}