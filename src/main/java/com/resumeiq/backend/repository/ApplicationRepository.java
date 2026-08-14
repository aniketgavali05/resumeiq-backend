package com.resumeiq.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByUserIdOrderByAppliedAtDesc(
            Long userId
    );

    List<Application> findByUserIdAndStatusOrderByAppliedAtDesc(
            Long userId,
            String status
    );

    boolean existsByUserIdAndJobId(
            Long userId,
            Long jobId
    );

    long countByUserId(
            Long userId
    );

    long countByUserIdAndStatus(
            Long userId,
            String status
    );
}