package com.resumeiq.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.Interview;

public interface InterviewRepository
        extends JpaRepository<Interview, Long> {

    List<Interview> findByUserIdOrderByScheduledAtAsc(
            Long userId
    );

    List<Interview> findByUserIdAndStatusOrderByScheduledAtAsc(
            Long userId,
            String status
    );

    Optional<Interview> findByUserIdAndApplicationId(
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

    long countByUserIdAndStatus(
            Long userId,
            String status
    );
}