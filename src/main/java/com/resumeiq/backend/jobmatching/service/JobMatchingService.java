package com.resumeiq.backend.jobmatching.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.resumeiq.backend.ats.service.KeywordAnalyzer;
import com.resumeiq.backend.ats.service.ResumeTextExtractor;
import com.resumeiq.backend.ats.service.SkillMatcher;
import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.entity.Resume;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.jobmatching.dto.JobMatchResponse;
import com.resumeiq.backend.jobmatching.dto.MatchResult;
import com.resumeiq.backend.jobmatching.repository.JobRepository;
import com.resumeiq.backend.repository.ResumeRepository;
import com.resumeiq.backend.repository.UserRepository;

@Service
public class JobMatchingService {

    private final SkillMatcher skillMatcher;
    private final KeywordAnalyzer keywordAnalyzer;
    private final SkillGapAnalyzer skillGapAnalyzer;
    private final MatchCalculator matchCalculator;
    private final ResumeTextExtractor resumeTextExtractor;

    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public JobMatchingService(
            SkillMatcher skillMatcher,
            KeywordAnalyzer keywordAnalyzer,
            SkillGapAnalyzer skillGapAnalyzer,
            MatchCalculator matchCalculator,
            ResumeTextExtractor resumeTextExtractor,
            JobRepository jobRepository,
            ResumeRepository resumeRepository,
            UserRepository userRepository) {

        this.skillMatcher = skillMatcher;
        this.keywordAnalyzer = keywordAnalyzer;
        this.skillGapAnalyzer = skillGapAnalyzer;
        this.matchCalculator = matchCalculator;
        this.resumeTextExtractor = resumeTextExtractor;

        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Existing single-job matching endpoint.
     *
     * Kept so we do not break the existing frontend/API.
     */
    public JobMatchResponse matchJob(
            String resumeText,
            String jobDescription) {

        List<String> matchedSkills =
                skillMatcher.getMatchedSkills(
                        resumeText,
                        jobDescription);

        List<String> missingSkills =
                skillMatcher.getMissingSkills(
                        resumeText,
                        jobDescription);

        int skillScore =
                skillMatcher.calculateSkillScore(
                        resumeText,
                        jobDescription);

        int keywordScore =
                keywordAnalyzer.calculateKeywordScore(
                        resumeText,
                        jobDescription);

        int experienceScore =
                matchCalculator.calculateExperienceScore(
                        resumeText,
                        jobDescription);

        int educationScore =
                matchCalculator.calculateEducationScore(
                        resumeText,
                        jobDescription);

        /*
         * For this legacy endpoint we do not have a stored ATS
         * score, so use the available resume/job matching signals.
         */
        int atsScore =
                (skillScore + keywordScore) / 2;

        int matchScore =
                matchCalculator.calculateFinalScore(
                        skillScore,
                        keywordScore,
                        atsScore,
                        experienceScore,
                        educationScore);

        JobMatchResponse response =
                new JobMatchResponse();

        response.setMatchScore(matchScore);
        response.setMatchedSkills(matchedSkills);
        response.setMissingSkills(missingSkills);
        response.setRecommendations(
                skillGapAnalyzer.buildRecommendations(
                        missingSkills));

        return response;
    }

    /**
     * Calculate real matches between the current user's latest
     * resume and every active job stored in PostgreSQL.
     */
    public List<MatchResult> getMatchesForCurrentUser() {

        User currentUser =
                getCurrentUser();

        Resume resume =
                resumeRepository
                        .findTopByUserIdOrderByUploadedAtDesc(
                                currentUser.getId());

        if (resume == null) {

            throw new ResourceNotFoundException(
                    "Please upload a resume before viewing job matches.");
        }

        String resumeText =
                extractStoredResumeText(resume);

        if (resumeText.isBlank()) {

            throw new IllegalStateException(
                    "Unable to extract text from your latest resume.");
        }

        int atsScore =
                resume.getResumeScore() == null
                        ? 0
                        : clamp(resume.getResumeScore());

        List<Job> jobs =
                jobRepository.findByActiveTrue();

        return jobs.stream()
                .map(job ->
                        calculateMatch(
                                job,
                                resumeText,
                                atsScore))
                .sorted(
                        Comparator.comparing(
                                MatchResult::getMatchScore,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Calculate one job match.
     */
    private MatchResult calculateMatch(
            Job job,
            String resumeText,
            int atsScore) {

        String jobText =
                buildJobText(job);

        List<String> matchedSkills =
                skillMatcher.getMatchedSkills(
                        resumeText,
                        jobText);

        List<String> missingSkills =
                skillMatcher.getMissingSkills(
                        resumeText,
                        jobText);

        int skillScore =
                skillMatcher.calculateSkillScore(
                        resumeText,
                        jobText);

        int keywordScore =
                keywordAnalyzer.calculateKeywordScore(
                        resumeText,
                        jobText);

        int experienceScore =
                matchCalculator.calculateExperienceScore(
                        resumeText,
                        jobText);

        int educationScore =
                matchCalculator.calculateEducationScore(
                        resumeText,
                        jobText);

        int finalScore =
                matchCalculator.calculateFinalScore(
                        skillScore,
                        keywordScore,
                        atsScore,
                        experienceScore,
                        educationScore);

        String recommendation =
                buildRecommendation(
                        finalScore,
                        matchedSkills,
                        missingSkills);

        return new MatchResult(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                finalScore,
                atsScore,
                matchedSkills,
                missingSkills,
                recommendation);
    }

    /**
     * Combine the fields that actually describe the job.
     */
    private String buildJobText(Job job) {

        StringBuilder text =
                new StringBuilder();

        append(text, job.getTitle());
        append(text, job.getCompany());
        append(text, job.getDescription());
        append(text, job.getLocation());
        append(text, job.getEmploymentType());
        append(text, job.getExperienceLevel());
        append(text, job.getSalaryRange());

        return text.toString();
    }

    private void append(
            StringBuilder builder,
            String value) {

        if (value != null && !value.isBlank()) {
            builder.append(value).append(" ");
        }
    }

    /**
     * Read the stored resume from the path saved in PostgreSQL.
     */
    private String extractStoredResumeText(
            Resume resume) {

        String storagePath =
                resume.getStoragePath();

        String storedFileName =
                resume.getStoredFileName();

        Path path;

        if (storagePath != null
                && !storagePath.isBlank()) {

            path =
                    Paths.get(storagePath);

        } else if (storedFileName != null
                && !storedFileName.isBlank()) {

            path =
                    Paths.get("uploads")
                            .resolve("resumes")
                            .resolve(storedFileName);

        } else {

            throw new IllegalStateException(
                    "Resume storage information is missing.");
        }

        if (!Files.exists(path)) {

            throw new IllegalStateException(
                    "Stored resume file was not found: "
                            + path.toAbsolutePath());
        }

        try {

            return resumeTextExtractor.extractText(
                    path,
                    resume.getOriginalFileName());

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Failed to read stored resume.",
                    e);
        }
    }

    private String buildRecommendation(
            int score,
            List<String> matchedSkills,
            List<String> missingSkills) {

        if (score >= 90) {

            return "Excellent match. Your resume strongly aligns with this job.";

        }

        if (score >= 80) {

            if (!missingSkills.isEmpty()) {

                return "Strong match. Consider strengthening: "
                        + String.join(", ", limit(missingSkills, 3))
                        + ".";

            }

            return "Strong match. Your skills align well with this role.";
        }

        if (score >= 70) {

            if (!missingSkills.isEmpty()) {

                return "Good match. Improve these areas: "
                        + String.join(", ", limit(missingSkills, 3))
                        + ".";

            }

            return "Good match with some room for improvement.";
        }

        if (score >= 50) {

            return "Moderate match. Review the missing skills before applying.";
        }

        return "Low match. Consider improving your resume for this role.";
    }

    private List<String> limit(
            List<String> values,
            int max) {

        return values.stream()
                .limit(max)
                .toList();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new ResourceNotFoundException(
                    "Authenticated user not found.");
        }

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."));
    }

    private int clamp(int score) {

        return Math.max(
                0,
                Math.min(100, score));
    }
}