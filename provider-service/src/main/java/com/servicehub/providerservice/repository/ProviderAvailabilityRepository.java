package com.servicehub.providerservice.repository;

import com.servicehub.providerservice.entity.ProviderAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface ProviderAvailabilityRepository extends JpaRepository<ProviderAvailability, Long> {
    List<ProviderAvailability> findByProviderProfileId(Long providerProfileId);
    Optional<ProviderAvailability> findByProviderProfileIdAndDayOfWeek(Long providerProfileId, DayOfWeek dayOfWeek);
}