package com.black.garlic.common.repository;

import com.black.garlic.common.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByProfileId(String profileId);
}
