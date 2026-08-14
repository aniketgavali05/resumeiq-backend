package com.resumeiq.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.jobmatching.repository.JobRepository;
import com.resumeiq.backend.request.CreateJobRequest;
import com.resumeiq.backend.response.JobResponse;

@Service
public class JobService {

    private static final Logger log =
            LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {

        this.jobRepository = jobRepository;
    }

    // ===========================
    // Public Methods
    // ===========================

    public List<JobResponse> getAllJobs() {

        log.info("Fetching all active jobs.");

        return jobRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long id) {

        log.info("Fetching job with id: {}", id);

        return mapToResponse(findJob(id));
    }

    public JobResponse createJob(CreateJobRequest request) {

        log.info("Creating job: {}", request.getTitle());

        Job job = new Job();

        populateJob(job, request);

        job.setActive(true);

        Job saved = jobRepository.save(job);

        log.info("Job created successfully with id: {}",
                saved.getId());

        return mapToResponse(saved);
    }

    public JobResponse updateJob(
            Long id,
            CreateJobRequest request) {

        log.info("Updating job with id: {}", id);

        Job job = findJob(id);

        populateJob(job, request);

        Job updated = jobRepository.save(job);

        log.info("Job updated successfully.");

        return mapToResponse(updated);
    }

    public void deleteJob(Long id) {

        log.info("Deleting job with id: {}", id);

        Job job = findJob(id);

        job.setActive(false);

        jobRepository.save(job);

        log.info("Job marked as inactive.");
    }

    // ===========================
    // Helper Methods
    // ===========================

    private Job findJob(Long id) {

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));
    }

    private void populateJob(
            Job job,
            CreateJobRequest request) {

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setSalaryRange(request.getSalaryRange());
        job.setApplyUrl(request.getApplyUrl());
    }

    private JobResponse mapToResponse(Job job) {

        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCompany(job.getCompany());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setExperienceLevel(job.getExperienceLevel());
        response.setSalaryRange(job.getSalaryRange());
        response.setApplyUrl(job.getApplyUrl());
        response.setActive(job.getActive());

        return response;
    }
}