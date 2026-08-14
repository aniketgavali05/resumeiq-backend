
package com.resumeiq.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.UserSettings;

public interface UserSettingsRepository
        extends JpaRepository<UserSettings, Long> {

    Optional<UserSettings> findByUserId(Long userId);
}

