package com.servicehub.providerservice.repository;

import com.servicehub.providerservice.entity.ProviderSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProviderSkillRepository extends JpaRepository<ProviderSkill, Long> {
    List<ProviderSkill> findByProviderProfileId(Long providerProfileId);
    Optional<ProviderSkill> findByIdAndProviderProfileId(Long id, Long providerProfileId);
}