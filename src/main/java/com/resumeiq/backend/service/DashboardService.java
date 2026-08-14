package com.resumeiq.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.resumeiq.backend.entity.Resume;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.jobmatching.dto.MatchResult;
import com.resumeiq.backend.jobmatching.service.JobMatchingService;
import com.resumeiq.backend.repository.ApplicationRepository;
import com.resumeiq.backend.repository.InterviewRepository;
import com.resumeiq.backend.repository.ResumeRepository;
import com.resumeiq.backend.repository.SavedJobRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.response.DashboardRecentResumeResponse;
import com.resumeiq.backend.response.DashboardStatsResponse;

@Service
public class DashboardService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final SavedJobRepository savedJobRepository;
    private final JobMatchingService jobMatchingService;

    public DashboardService(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            ApplicationRepository applicationRepository,
            InterviewRepository interviewRepository,
            SavedJobRepository savedJobRepository,
            JobMatchingService jobMatchingService) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.savedJobRepository = savedJobRepository;
        this.jobMatchingService = jobMatchingService;
    }

    public DashboardStatsResponse getDashboardStats() {

        User currentUser = getCurrentUser();

        Long userId = currentUser.getId();

        // ==========================================
        // Resume statistics
        // ==========================================

        Double averageScore =
                resumeRepository.getAverageScore(userId);

        Resume bestResume =
                resumeRepository.findTopByUserIdOrderByResumeScoreDesc(
                        userId
                );

        int resumeScore =
                averageScore == null
                        ? 0
                        : averageScore.intValue();

        // ==========================================
        // Real Job Match Score
        // ==========================================

        int matchScore = 0;

        try {
            List<MatchResult> matches =
                    jobMatchingService.getMatchesForCurrentUser();

            if (matches != null && !matches.isEmpty()) {

                MatchResult bestMatch =
                        matches.get(0);

                if (bestMatch.getMatchScore() != null) {
                    matchScore =
                            Math.max(
                                    0,
                                    Math.min(
                                            100,
                                            bestMatch.getMatchScore()
                                    )
                            );
                }
            }

        } catch (Exception e) {

            // Matching should not prevent the dashboard
            // from loading if the user has no valid resume
            // or a stored resume cannot currently be read.
            matchScore = 0;
        }

        // ==========================================
        // Application statistics
        // ==========================================

        int applications =
                Math.toIntExact(
                        applicationRepository.countByUserId(
                                userId
                        )
                );

        // ==========================================
        // Real interview statistics
        // ==========================================

        int interviews =
                Math.toIntExact(
                        interviewRepository.countByUserId(
                                userId
                        )
                );

        // ==========================================
        // Offer statistics
        // ==========================================

        int offers =
                Math.toIntExact(
                        applicationRepository
                                .countByUserIdAndStatus(
                                        userId,
                                        "OFFER"
                                )
                );

        // ==========================================
        // Saved Jobs statistics
        // ==========================================

        int savedJobs =
                Math.toIntExact(
                        savedJobRepository.countByUserId(
                                userId
                        )
                );

        // ==========================================
        // Recent resumes
        // ==========================================

        List<DashboardRecentResumeResponse> recentResumes =
                resumeRepository
                        .findTop5ByUserIdOrderByUploadedAtDesc(
                                userId
                        )
                        .stream()
                        .map(resume ->
                                new DashboardRecentResumeResponse(
                                        resume.getId(),
                                        resume.getOriginalFileName(),
                                        resume.getResumeScore(),
                                        resume.getUploadedAt()
                                )
                        )
                        .collect(Collectors.toList());

        // ==========================================
        // Dashboard response
        // ==========================================

        return new DashboardStatsResponse(
                applications,   // Applications
                interviews,     // Interviews
                offers,         // Offers
                savedJobs,      // Saved Jobs

                resumeScore,    // Average ATS Score
                matchScore,     // Best Job Match Score

                recentResumes
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