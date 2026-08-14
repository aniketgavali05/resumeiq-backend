package com.resumeiq.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeiq.backend.entity.ResumeAnalysis;

@Repository
public interface ResumeAnalysisRepository
        extends JpaRepository<ResumeAnalysis, Long> {

    Optional<ResumeAnalysis> findByResumeId(Long resumeId);
}