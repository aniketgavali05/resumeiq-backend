package com.resumeiq.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumeiq.backend.entity.Application;
import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.exception.DuplicateResourceException;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.jobmatching.repository.JobRepository;
import com.resumeiq.backend.repository.ApplicationRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.request.CreateApplicationRequest;
import com.resumeiq.backend.response.ApplicationResponse;

@Service
@Transactional
public class ApplicationService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    ApplicationService.class
            );

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final NotificationService notificationService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            NotificationService notificationService
    ) {
        this.applicationRepository =
                applicationRepository;

        this.userRepository =
                userRepository;

        this.jobRepository =
                jobRepository;

        this.notificationService =
                notificationService;
    }

    // =====================================================
    // Create Application
    // =====================================================

    public ApplicationResponse createApplication(
            CreateApplicationRequest request
    ) {

        User currentUser =
                getCurrentUser();

        Job job =
                jobRepository.findById(
                        request.getJobId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found."
                        )
                );

        if (!Boolean.TRUE.equals(
                job.getActive()
        )) {

            throw new IllegalArgumentException(
                    "This job is no longer active."
            );
        }

        if (applicationRepository
                .existsByUserIdAndJobId(
                        currentUser.getId(),
                        job.getId()
                )) {

            throw new DuplicateResourceException(
                    "You have already applied for this job."
            );
        }

        Application application =
                new Application();

        application.setUser(
                currentUser
        );

        application.setJob(
                job
        );

        application.setStatus(
                "APPLIED"
        );

        application.setNotes(
                request.getNotes()
        );

        Application saved =
                applicationRepository.save(
                        application
                );

        log.info(
                "Application created. applicationId={}, userId={}, jobId={}",
                saved.getId(),
                currentUser.getId(),
                job.getId()
        );

        // ==========================================
        // Notification: Application submitted
        // ==========================================

        notificationService.createNotification(
                currentUser,
                "Application submitted",
                "Your application for "
                        + job.getTitle()
                        + " at "
                        + job.getCompany()
                        + " was successfully submitted.",
                "success"
        );

        return mapToResponse(saved);
    }

    // =====================================================
    // Get Current User Applications
    // =====================================================

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {

        User currentUser =
                getCurrentUser();

        return applicationRepository
                .findByUserIdOrderByAppliedAtDesc(
                        currentUser.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // Get Single Application
    // =====================================================

    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(
            Long id
    ) {

        User currentUser =
                getCurrentUser();

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found."
                                )
                        );

        verifyOwnership(
                application,
                currentUser
        );

        return mapToResponse(
                application
        );
    }

    // =====================================================
    // Update Application Status
    // =====================================================

    public ApplicationResponse updateStatus(
            Long id,
            String status
    ) {

        User currentUser =
                getCurrentUser();

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found."
                                )
                        );

        verifyOwnership(
                application,
                currentUser
        );

        String previousStatus =
                application.getStatus();

        String normalizedStatus =
                status == null
                        ? ""
                        : status.trim().toUpperCase();

        validateStatus(
                normalizedStatus
        );

        // No database update or notification
        // when the status hasn't changed.
        if (normalizedStatus.equals(
                previousStatus
        )) {
            return mapToResponse(
                    application
            );
        }

        application.setStatus(
                normalizedStatus
        );

        Application updated =
                applicationRepository.save(
                        application
                );

        log.info(
                "Application status updated. applicationId={}, previousStatus={}, status={}",
                id,
                previousStatus,
                normalizedStatus
        );

        // ==========================================
        // Notification: Status changed
        // ==========================================

        Job job =
                application.getJob();

        String jobTitle =
                job != null && job.getTitle() != null
                        ? job.getTitle()
                        : "your application";

        String company =
                job != null && job.getCompany() != null
                        ? job.getCompany()
                        : "the employer";

        String notificationType =
                getNotificationType(
                        normalizedStatus
                );

        notificationService.createNotification(
                currentUser,
                "Application status updated",
                "Your application for "
                        + jobTitle
                        + " at "
                        + company
                        + " changed from "
                        + previousStatus
                        + " to "
                        + normalizedStatus
                        + ".",
                notificationType
        );

        return mapToResponse(
                updated
        );
    }

    // =====================================================
    // Notification Type
    // =====================================================

    private String getNotificationType(
            String status
    ) {

        switch (status) {

            case "INTERVIEW":
                return "success";

            case "OFFER":
                return "success";

            case "REJECTED":
                return "warning";

            case "WITHDRAWN":
                return "info";

            case "APPLIED":
            default:
                return "info";
        }
    }

    // =====================================================
    // Delete Application
    // =====================================================

    public void deleteApplication(
            Long id
    ) {

        User currentUser =
                getCurrentUser();

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found."
                                )
                        );

        verifyOwnership(
                application,
                currentUser
        );

        applicationRepository.delete(
                application
        );

        log.info(
                "Application deleted. applicationId={}, userId={}",
                id,
                currentUser.getId()
        );
    }

    // =====================================================
    // Current User
    // =====================================================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new ResourceNotFoundException(
                    "Authenticated user not found."
            );
        }

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        ));
    }

    // =====================================================
    // Ownership
    // =====================================================

    private void verifyOwnership(
            Application application,
            User currentUser
    ) {

        if (application.getUser() == null
                || application.getUser().getId() == null
                || !application.getUser()
                        .getId()
                        .equals(
                                currentUser.getId()
                        )) {

            throw new ResourceNotFoundException(
                    "Application not found."
            );
        }
    }

    // =====================================================
    // Status Validation
    // =====================================================

    private void validateStatus(
            String status
    ) {

        switch (status) {

            case "APPLIED":
            case "INTERVIEW":
            case "OFFER":
            case "REJECTED":
            case "WITHDRAWN":
                return;

            default:
                throw new IllegalArgumentException(
                        "Invalid application status. "
                        + "Allowed values: APPLIED, INTERVIEW, "
                        + "OFFER, REJECTED, WITHDRAWN."
                );
        }
    }

    // =====================================================
    // Mapping
    // =====================================================

    private ApplicationResponse mapToResponse(
            Application application
    ) {

        ApplicationResponse response =
                new ApplicationResponse();

        Job job =
                application.getJob();

        response.setId(
                application.getId()
        );

        if (job != null) {

            response.setJobId(
                    job.getId()
            );

            response.setJobTitle(
                    job.getTitle()
            );

            response.setCompany(
                    job.getCompany()
            );

            response.setLocation(
                    job.getLocation()
            );

            response.setEmploymentType(
                    job.getEmploymentType()
            );

            response.setExperienceLevel(
                    job.getExperienceLevel()
            );

            response.setSalaryRange(
                    job.getSalaryRange()
            );

            response.setApplyUrl(
                    job.getApplyUrl()
            );
        }

        response.setStatus(
                application.getStatus()
        );

        response.setNotes(
                application.getNotes()
        );

        response.setAppliedAt(
                application.getAppliedAt()
        );

        response.setCreatedAt(
                application.getCreatedAt()
        );

        response.setUpdatedAt(
                application.getUpdatedAt()
        );

        return response;
    }
}