package com.resumeiq.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.SavedJob;

public interface SavedJobRepository
        extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    Optional<SavedJob> findByUserIdAndJobId(
            Long userId,
            Long jobId
    );

    boolean existsByUserIdAndJobId(
            Long userId,
            Long jobId
    );

    long countByUserId(
            Long userId
    );

    void deleteByUserIdAndJobId(
            Long userId,
            Long jobId
    );
}