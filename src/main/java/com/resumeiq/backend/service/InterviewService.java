package com.resumeiq.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumeiq.backend.entity.Application;
import com.resumeiq.backend.entity.Interview;
import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.repository.ApplicationRepository;
import com.resumeiq.backend.repository.InterviewRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.response.InterviewResponse;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public InterviewService(
            InterviewRepository interviewRepository,
            ApplicationRepository applicationRepository,
            UserRepository userRepository
    ) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InterviewResponse createInterview(
            Long applicationId,
            String interviewType,
            LocalDateTime scheduledAt,
            String interviewerName,
            String meetingLink,
            String location,
            String notes
    ) {
        User currentUser = getCurrentUser();

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found."
                                ));

        if (!application.getUser().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException(
                    "You are not allowed to access this application."
            );
        }

        Interview interview =
                interviewRepository
                        .findByUserIdAndApplicationId(
                                currentUser.getId(),
                                applicationId
                        )
                        .orElseGet(Interview::new);

        interview.setUser(currentUser);
        interview.setApplication(application);

        interview.setInterviewType(
                interviewType == null ||
                interviewType.isBlank()
                        ? "VIDEO"
                        : interviewType
        );

        if (scheduledAt == null) {
            throw new RuntimeException(
                    "Interview date and time are required."
            );
        }

        interview.setScheduledAt(scheduledAt);
        interview.setInterviewerName(interviewerName);
        interview.setMeetingLink(meetingLink);
        interview.setLocation(location);
        interview.setNotes(notes);

        interview.setStatus("SCHEDULED");

        Interview saved =
                interviewRepository.save(interview);

        // Keep application status synchronized.
        application.setStatus("INTERVIEW");
        applicationRepository.save(application);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getMyInterviews() {

        User currentUser = getCurrentUser();

        return interviewRepository
                .findByUserIdOrderByScheduledAtAsc(
                        currentUser.getId()
                )
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(
            Long id
    ) {
        User currentUser = getCurrentUser();

        Interview interview =
                interviewRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Interview not found."
                                ));

        if (!interview.getUser().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException(
                    "You are not allowed to access this interview."
            );
        }

        return toResponse(interview);
    }

    @Transactional
    public InterviewResponse updateStatus(
            Long id,
            String status
    ) {
        User currentUser = getCurrentUser();

        Interview interview =
                interviewRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Interview not found."
                                ));

        if (!interview.getUser().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException(
                    "You are not allowed to access this interview."
            );
        }

        if (status == null || status.isBlank()) {
            throw new RuntimeException(
                    "Interview status is required."
            );
        }

        interview.setStatus(
                status.trim().toUpperCase()
        );

        Interview saved =
                interviewRepository.save(interview);

        return toResponse(saved);
    }

    @Transactional
    public void deleteInterview(
            Long id
    ) {
        User currentUser = getCurrentUser();

        Interview interview =
                interviewRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Interview not found."
                                ));

        if (!interview.getUser().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException(
                    "You are not allowed to access this interview."
            );
        }

        interviewRepository.delete(interview);
    }

    @Transactional(readOnly = true)
    public long countMyInterviews() {

        User currentUser = getCurrentUser();

        return interviewRepository.countByUserId(
                currentUser.getId()
        );
    }

    private InterviewResponse toResponse(
            Interview interview
    ) {
        Application application =
                interview.getApplication();

        Job job =
                application.getJob();

        return new InterviewResponse(
                interview.getId(),
                application.getId(),
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                interview.getInterviewType(),
                interview.getScheduledAt(),
                interview.getInterviewerName(),
                interview.getMeetingLink(),
                interview.getLocation(),
                interview.getNotes(),
                interview.getStatus(),
                interview.getCreatedAt(),
                interview.getUpdatedAt()
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