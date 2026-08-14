package com.resumeiq.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.resumeiq.backend.ats.constants.SkillsDatabase;
import com.resumeiq.backend.ats.service.ResumeTextExtractor;
import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.entity.Resume;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.jobmatching.repository.JobRepository;
import com.resumeiq.backend.repository.ResumeRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.response.SkillAnalysisResponse;
import com.resumeiq.backend.response.SkillAnalysisResponse.SkillItem;

@Service
public class ResumeSkillAnalysisService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeTextExtractor resumeTextExtractor;

    public ResumeSkillAnalysisService(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            ResumeTextExtractor resumeTextExtractor
    ) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.resumeTextExtractor = resumeTextExtractor;
    }

    public SkillAnalysisResponse analyzeCurrentUser() {

        User currentUser = getCurrentUser();

        Resume resume =
                resumeRepository
                        .findTopByUserIdOrderByUploadedAtDesc(
                                currentUser.getId()
                        );

        if (resume == null) {
            throw new ResourceNotFoundException(
                    "Please upload a resume before viewing your skills."
            );
        }

        String resumeText =
                extractResumeText(resume);

        if (resumeText == null
                || resumeText.isBlank()) {
            throw new IllegalStateException(
                    "Unable to extract text from your latest resume."
            );
        }

        List<Job> activeJobs =
                jobRepository.findByActiveTrue();

        List<SkillItem> results =
                analyzeSkills(
                        resumeText,
                        activeJobs
                );

        return new SkillAnalysisResponse(
                results
        );
    }

    private List<SkillItem> analyzeSkills(
            String resumeText,
            List<Job> activeJobs
    ) {

        String resume =
                resumeText.toLowerCase();

        List<Job> jobs =
                activeJobs == null
                        ? List.of()
                        : activeJobs;

        int totalJobs =
                jobs.size();

        List<SkillItem> results =
                new ArrayList<>();

        if (totalJobs == 0) {
            return results;
        }

        for (String skill :
                SkillsDatabase.SKILLS) {

            String normalizedSkill =
                    skill.toLowerCase();

            int requiredJobCount = 0;

            for (Job job : jobs) {

                String jobText =
                        buildJobText(job)
                                .toLowerCase();

                if (jobText.contains(
                        normalizedSkill
                )) {
                    requiredJobCount++;
                }
            }

            if (requiredJobCount == 0) {
                continue;
            }

            int requiredLevel =
                    Math.round(
                            (
                                requiredJobCount
                                * 100f
                            )
                            / totalJobs
                    );

            int currentLevel =
                    resume.contains(
                            normalizedSkill
                    )
                            ? 100
                            : 0;

            results.add(
                    new SkillItem(
                            skill,
                            currentLevel,
                            requiredLevel,
                            getCategory(skill)
                    )
            );
        }

        results.sort(
                (a, b) ->
                        Integer.compare(
                                b.getRequiredLevel(),
                                a.getRequiredLevel()
                        )
        );

        return results;
    }

    private String buildJobText(
            Job job
    ) {

        StringBuilder text =
                new StringBuilder();

        append(
                text,
                job.getTitle()
        );

        append(
                text,
                job.getCompany()
        );

        append(
                text,
                job.getDescription()
        );

        append(
                text,
                job.getLocation()
        );

        append(
                text,
                job.getEmploymentType()
        );

        append(
                text,
                job.getExperienceLevel()
        );

        append(
                text,
                job.getSalaryRange()
        );

        return text.toString();
    }

    private void append(
            StringBuilder builder,
            String value
    ) {

        if (value != null
                && !value.isBlank()) {

            builder.append(value)
                    .append(" ");
        }
    }

    private String getCategory(
            String skill
    ) {

        String normalized =
                skill.toLowerCase();

        if (List.of(
                "java",
                "python",
                "javascript",
                "typescript",
                "c",
                "c++",
                "c#",
                "go",
                "rust",
                "kotlin",
                "swift",
                "php",
                "ruby"
        ).contains(normalized)) {
            return "Programming";
        }

        if (List.of(
                "html",
                "css",
                "bootstrap",
                "tailwind css",
                "tailwind",
                "react",
                "next.js",
                "nextjs",
                "angular",
                "vue",
                "redux"
        ).contains(normalized)) {
            return "Frontend";
        }

        if (List.of(
                "spring",
                "spring boot",
                "spring security",
                "hibernate",
                "jpa",
                "servlet",
                "jsp",
                "rest api",
                "graphql",
                "microservices"
        ).contains(normalized)) {
            return "Backend";
        }

        if (List.of(
                "mysql",
                "postgresql",
                "oracle",
                "mongodb",
                "redis",
                "firebase",
                "sqlite"
        ).contains(normalized)) {
            return "Database";
        }

        if (List.of(
                "aws",
                "azure",
                "gcp"
        ).contains(normalized)) {
            return "Cloud";
        }

        if (List.of(
                "docker",
                "kubernetes",
                "jenkins",
                "github actions",
                "terraform",
                "ansible"
        ).contains(normalized)) {
            return "DevOps";
        }

        if (List.of(
                "kafka",
                "rabbitmq"
        ).contains(normalized)) {
            return "Messaging";
        }

        if (List.of(
                "git",
                "github",
                "gitlab"
        ).contains(normalized)) {
            return "Version Control";
        }

        if (List.of(
                "maven",
                "gradle"
        ).contains(normalized)) {
            return "Build Tools";
        }

        if (List.of(
                "junit",
                "mockito",
                "selenium",
                "postman"
        ).contains(normalized)) {
            return "Testing";
        }

        if (List.of(
                "jwt",
                "oauth2"
        ).contains(normalized)) {
            return "Authentication";
        }

        if (List.of(
                "openai",
                "gemini",
                "langchain",
                "huggingface",
                "machine learning",
                "deep learning"
        ).contains(normalized)) {
            return "AI";
        }

        if (List.of(
                "leadership",
                "communication",
                "teamwork",
                "problem solving",
                "analytical thinking"
        ).contains(normalized)) {
            return "Soft Skills";
        }

        return "Other";
    }

    private String extractResumeText(
            Resume resume
    ) {

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
                    "Resume storage information is missing."
            );
        }

        if (!Files.exists(path)) {

            throw new IllegalStateException(
                    "Stored resume file was not found: "
                            + path.toAbsolutePath()
            );
        }

        try {

            return resumeTextExtractor.extractText(
                    path,
                    resume.getOriginalFileName()
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Failed to read stored resume.",
                    e
            );
        }
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
}