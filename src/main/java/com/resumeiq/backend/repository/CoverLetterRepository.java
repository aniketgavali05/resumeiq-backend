package com.resumeiq.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.CoverLetter;

public interface CoverLetterRepository
        extends JpaRepository<CoverLetter, Long> {

    List<CoverLetter>
    findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    Optional<CoverLetter>
    findByUserIdAndApplicationId(
            Long userId,
            Long applicationId
    );

    boolean existsByUserIdAndApplicationId(
            Long userId,
            Long applicationId
    );

    long countByUserId(
            Long userId
    );
}