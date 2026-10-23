package com.servicehub.userservice.repository;

import com.servicehub.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByAuthUserId(String authUserId);
    boolean existsByAuthUserId(String authUserId);
}