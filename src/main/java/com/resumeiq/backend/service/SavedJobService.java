package com.resumeiq.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.entity.SavedJob;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.jobmatching.repository.JobRepository;
import com.resumeiq.backend.repository.SavedJobRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.response.SavedJobResponse;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public SavedJobService(
            SavedJobRepository savedJobRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SavedJobResponse saveJob(Long jobId) {

        User currentUser = getCurrentUser();

        SavedJob existing =
                savedJobRepository
                        .findByUserIdAndJobId(
                                currentUser.getId(),
                                jobId
                        )
                        .orElse(null);

        if (existing != null) {
            return toResponse(existing);
        }

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found."
                                ));

        SavedJob savedJob = new SavedJob();

        savedJob.setUser(currentUser);
        savedJob.setJob(job);

        SavedJob saved =
                savedJobRepository.save(savedJob);

        return toResponse(saved);
    }

    @Transactional
    public void unsaveJob(Long jobId) {

        User currentUser = getCurrentUser();

        savedJobRepository.deleteByUserIdAndJobId(
                currentUser.getId(),
                jobId
        );
    }

    @Transactional(readOnly = true)
    public List<SavedJobResponse> getMySavedJobs() {

        User currentUser = getCurrentUser();

        return savedJobRepository
                .findByUserIdOrderByCreatedAtDesc(
                        currentUser.getId()
                )
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isJobSaved(Long jobId) {

        User currentUser = getCurrentUser();

        return savedJobRepository
                .existsByUserIdAndJobId(
                        currentUser.getId(),
                        jobId
                );
    }

    @Transactional(readOnly = true)
    public long countMySavedJobs() {

        User currentUser = getCurrentUser();

        return savedJobRepository.countByUserId(
                currentUser.getId()
        );
    }

    private SavedJobResponse toResponse(
            SavedJob savedJob
    ) {

        Job job = savedJob.getJob();

        return new SavedJobResponse(
                savedJob.getId(),
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getDescription(),
                job.getEmploymentType(),
                job.getExperienceLevel(),
                job.getSalaryRange(),
                job.getApplyUrl(),
                job.getActive(),
                savedJob.getCreatedAt()
        );
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "Authenticated user not found."
            );
        }

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found."
                        ));
    }
}