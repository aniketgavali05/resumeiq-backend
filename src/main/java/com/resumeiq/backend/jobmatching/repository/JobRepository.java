package com.resumeiq.backend.jobmatching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByActiveTrue();

    List<Job> findByActiveTrueOrderByCreatedAtDesc();

    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByCompanyContainingIgnoreCase(String company);

    List<Job> findByLocationContainingIgnoreCase(String location);
}