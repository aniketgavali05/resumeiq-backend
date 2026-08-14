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
import com.resumeiq.backend.response.RoadmapResponse;
import com.resumeiq.backend.response.RoadmapResponse.RoadmapItem;

@Service
public class ResumeRoadmapService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeTextExtractor resumeTextExtractor;

    public ResumeRoadmapService(
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

    public RoadmapResponse getCurrentUserRoadmap() {

        User currentUser = getCurrentUser();

        Resume resume =
                resumeRepository
                        .findTopByUserIdOrderByUploadedAtDesc(
                                currentUser.getId()
                        );

        if (resume == null) {
            throw new ResourceNotFoundException(
                    "Please upload a resume before viewing your roadmap."
            );
        }

        String resumeText =
                extractStoredResumeText(resume);

        if (resumeText == null
                || resumeText.isBlank()) {
            throw new IllegalStateException(
                    "Unable to extract text from your latest resume."
            );
        }

        List<Job> activeJobs =
                jobRepository.findByActiveTrue();

        List<RoadmapItem> roadmap =
                buildRoadmap(
                        resumeText,
                        activeJobs
                );

        return new RoadmapResponse(roadmap);
    }

    private List<RoadmapItem> buildRoadmap(
            String resumeText,
            List<Job> activeJobs
    ) {

        List<RoadmapItem> roadmap =
                new ArrayList<>();

        String resume =
                resumeText.toLowerCase();

        List<MissingSkill> missingSkills =
                findMissingSkills(
                        resume,
                        activeJobs
                );

        int maxWeeks =
                Math.min(
                        missingSkills.size(),
                        8
                );

        for (int i = 0; i < maxWeeks; i++) {

            MissingSkill missingSkill =
                    missingSkills.get(i);

            int week =
                    i + 1;

            String status =
                    week == 1
                            ? "in-progress"
                            : "pending";

            roadmap.add(
                    new RoadmapItem(
                            "skill-" + week,
                            "Learn " + missingSkill.skill,
                            buildDescription(
                                    missingSkill.skill,
                                    missingSkill.category
                            ),
                            status,
                            week,
                            missingSkill.category
                    )
            );
        }

        if (roadmap.isEmpty()) {

            roadmap.add(
                    new RoadmapItem(
                            "maintenance-1",
                            "Maintain Your Skills",
                            "Your current skills already align well with the active jobs. Keep building projects and practicing key concepts.",
                            "in-progress",
                            1,
                            "Continuous Learning"
                    )
            );
        }

        return roadmap;
    }

    private List<MissingSkill> findMissingSkills(
            String resume,
            List<Job> activeJobs
    ) {

        List<MissingSkill> missingSkills =
                new ArrayList<>();

        if (activeJobs == null
                || activeJobs.isEmpty()) {
            return missingSkills;
        }

        for (String skill :
                SkillsDatabase.SKILLS) {

            String normalizedSkill =
                    skill.toLowerCase();

            int demandCount = 0;

            for (Job job :
                    activeJobs) {

                String jobText =
                        buildJobText(job)
                                .toLowerCase();

                if (jobText.contains(
                        normalizedSkill
                )) {

                    demandCount++;
                }
            }

            if (demandCount == 0
                    || resume.contains(
                            normalizedSkill
                    )) {
                continue;
            }

            missingSkills.add(
                    new MissingSkill(
                            skill,
                            getCategory(skill),
                            demandCount
                    )
            );
        }

        missingSkills.sort(
                (a, b) ->
                        Integer.compare(
                                b.demandCount,
                                a.demandCount
                        )
        );

        return missingSkills;
    }

    private String buildDescription(
            String skill,
            String category
    ) {

        return "Improve your "
                + category
                + " skills by learning "
                + skill
                + " and building a practical project that demonstrates it.";
    }

    private String buildJobText(
            Job job
    ) {

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
            String value
    ) {

        if (value != null
                && !value.isBlank()) {

            builder.append(value)
                    .append(" ");
        }
    }

    private String extractStoredResumeText(
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

    private static class MissingSkill {

        private final String skill;
        private final String category;
        private final int demandCount;

        private MissingSkill(
                String skill,
                String category,
                int demandCount
        ) {
            this.skill = skill;
            this.category = category;
            this.demandCount = demandCount;
        }
    }
}