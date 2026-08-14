package com.resumeiq.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumeiq.backend.entity.Application;
import com.resumeiq.backend.entity.CoverLetter;
import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.repository.ApplicationRepository;
import com.resumeiq.backend.repository.CoverLetterRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.request.CreateCoverLetterRequest;
import com.resumeiq.backend.response.CoverLetterResponse;

@Service
public class CoverLetterService {

    private final CoverLetterRepository coverLetterRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public CoverLetterService(
            CoverLetterRepository coverLetterRepository,
            ApplicationRepository applicationRepository,
            UserRepository userRepository
    ) {
        this.coverLetterRepository =
                coverLetterRepository;
        this.applicationRepository =
                applicationRepository;
        this.userRepository =
                userRepository;
    }

    @Transactional
    public CoverLetterResponse createCoverLetter(
            CreateCoverLetterRequest request
    ) {

        User currentUser =
                getCurrentUser();

        Application application =
                applicationRepository
                        .findById(
                                request.getApplicationId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found."
                                )
                        );

        if (!application.getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new ResourceNotFoundException(
                    "Application not found."
            );
        }

        String tone =
                request.getTone() == null
                        || request.getTone().isBlank()
                        ? "Professional"
                        : request.getTone().trim();

        CoverLetter coverLetter =
                coverLetterRepository
                        .findByUserIdAndApplicationId(
                                currentUser.getId(),
                                application.getId()
                        )
                        .orElseGet(CoverLetter::new);

        coverLetter.setUser(currentUser);
        coverLetter.setApplication(application);
        coverLetter.setTone(tone);

        coverLetter.setContent(
                generateContent(
                        application,
                        tone
                )
        );

        CoverLetter saved =
                coverLetterRepository.save(
                        coverLetter
                );

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CoverLetterResponse>
    getMyCoverLetters() {

        User currentUser =
                getCurrentUser();

        return coverLetterRepository
                .findByUserIdOrderByCreatedAtDesc(
                        currentUser.getId()
                )
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CoverLetterResponse
    getCoverLetterById(Long id) {

        User currentUser =
                getCurrentUser();

        CoverLetter letter =
                coverLetterRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cover letter not found."
                                )
                        );

        if (!letter.getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new ResourceNotFoundException(
                    "Cover letter not found."
            );
        }

        return toResponse(letter);
    }

    @Transactional
    public void deleteCoverLetter(
            Long id
    ) {

        User currentUser =
                getCurrentUser();

        CoverLetter letter =
                coverLetterRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cover letter not found."
                                )
                        );

        if (!letter.getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new ResourceNotFoundException(
                    "Cover letter not found."
            );
        }

        coverLetterRepository.delete(
                letter
        );
    }

    private CoverLetterResponse toResponse(
            CoverLetter letter
    ) {

        Application application =
                letter.getApplication();

        Job job =
                application.getJob();

        return new CoverLetterResponse(
                letter.getId(),
                application.getId(),
                job.getTitle(),
                job.getCompany(),
                letter.getTone(),
                letter.getContent(),
                letter.getCreatedAt()
        );
    }

    private String generateContent(
            Application application,
            String tone
    ) {

        Job job =
                application.getJob();

        String title =
                job.getTitle();

        String company =
                job.getCompany();

        String location =
                job.getLocation();

        String description =
                job.getDescription();

        String opening;

        switch (
                tone.toLowerCase()
        ) {
            case "enthusiastic":
                opening =
                        "I am excited to apply for the "
                        + title
                        + " position at "
                        + company
                        + ".";
                break;

            case "confident":
                opening =
                        "I am confident that my skills and experience make me a strong candidate for the "
                        + title
                        + " position at "
                        + company
                        + ".";
                break;

            case "friendly":
                opening =
                        "I would love the opportunity to join "
                        + company
                        + " as a "
                        + title
                        + ".";
                break;

            default:
                opening =
                        "I am writing to express my interest in the "
                        + title
                        + " position at "
                        + company
                        + ".";
        }

        StringBuilder content =
                new StringBuilder();

        content.append(opening)
                .append("\n\n");

        content.append(
                "I am particularly interested in this opportunity because it aligns with my professional goals and the kind of work I am looking to contribute to."
        ).append("\n\n");

        if (description != null
                && !description.isBlank()) {

            String summary =
                    description.length() > 500
                            ? description.substring(
                                    0,
                                    500
                              )
                            : description;

            content.append(
                    "Based on the role requirements, I am especially interested in contributing to the responsibilities and technologies highlighted for this position. "
            );

            content.append(
                    "I would welcome the opportunity to bring a practical, problem-solving mindset to the team."
            ).append("\n\n");
        }

        content.append(
                "I would appreciate the opportunity to discuss how my background and skills could contribute to "
        );

        content.append(company)
                .append(
                        " and the goals of this role."
                )
                .append("\n\n");

        content.append(
                "Thank you for considering my application. I look forward to the opportunity to speak with you."
        );

        if (location != null
                && !location.isBlank()) {
            content.append("\n\n");
        }

        return content.toString();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
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
                        )
                );
    }
}