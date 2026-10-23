package com.servicehub.userservice.repository;

import com.servicehub.userservice.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserProfileId(Long userProfileId);
    Optional<UserAddress> findByIdAndUserProfileId(Long id, Long userProfileId);
    Optional<UserAddress> findByUserProfileIdAndIsDefaultTrue(Long userProfileId);
}