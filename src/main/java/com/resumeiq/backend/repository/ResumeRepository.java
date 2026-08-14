package com.resumeiq.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resumeiq.backend.entity.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserId(Long userId);

    long countByUserId(Long userId);

    Resume findTopByUserIdOrderByResumeScoreDesc(Long userId);

    Resume findTopByUserIdOrderByUploadedAtDesc(Long userId);

    List<Resume> findTop5ByUserIdOrderByUploadedAtDesc(Long userId);

    @Query("""
        SELECT AVG(r.resumeScore)
        FROM Resume r
        WHERE r.user.id = :userId
    """)
    Double getAverageScore(@Param("userId") Long userId);
}