package com.resumeiq.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.ats.dto.ATSResponse;
import com.resumeiq.backend.ats.service.ATSService;
import com.resumeiq.backend.entity.Resume;
import com.resumeiq.backend.entity.ResumeAnalysis;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.repository.ResumeAnalysisRepository;
import com.resumeiq.backend.repository.ResumeRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.response.ResumePageResponse;
import com.resumeiq.backend.response.ResumeResponse;
import com.resumeiq.backend.util.FileUtils;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ResumeService {

    private static final Logger log =
            LoggerFactory.getLogger(ResumeService.class);

    private static final long MAX_FILE_SIZE =
            5 * 1024 * 1024;

    private final ResumeRepository resumeRepository;
private final ResumeAnalysisRepository resumeAnalysisRepository;
private final UserRepository userRepository;
private final ATSService atsService;

    private final Path uploadPath =
            Paths.get("uploads/resumes");

    public ResumeService(
        ResumeRepository resumeRepository,
        ResumeAnalysisRepository resumeAnalysisRepository,
        UserRepository userRepository,
        ATSService atsService) {

    this.resumeRepository = resumeRepository;
    this.resumeAnalysisRepository = resumeAnalysisRepository;
    this.userRepository = userRepository;
    this.atsService = atsService;

    createUploadDirectory();
}

    // =====================================================
    // Upload Resume
    // =====================================================

    public ResumeResponse uploadResume(
            MultipartFile file,
            String jobDescription)
            throws IOException {

        log.info("Resume upload started.");

        validateFile(file);

        User currentUser = getCurrentUser();

        String storedFileName = saveFile(file);

        Resume resume = buildResume(file, storedFileName);
        resume.setUser(currentUser);

        Resume savedResume = resumeRepository.save(resume);

        ATSResponse ats;

        try {
            ats = atsService.analyze(file, jobDescription);
        } catch (Exception e) {
            log.error("ATS analysis failed", e);
            throw new RuntimeException("ATS analysis failed: " + e.getMessage(), e);
        }

        savedResume.setResumeScore(ats.getScore());

        savedResume = resumeRepository.save(savedResume);
        // Save complete ATS analysis to PostgreSQL
        saveResumeAnalysis(savedResume, ats);

        ResumeResponse response = new ResumeResponse();

        response.setId(savedResume.getId());
        response.setOriginalFileName(savedResume.getOriginalFileName());
        response.setStoredFileName(savedResume.getStoredFileName());
        response.setFileType(savedResume.getFileType());
        response.setFileSize(savedResume.getFileSize());
        response.setResumeScore(savedResume.getResumeScore());
        response.setUploadedAt(savedResume.getUploadedAt());

        response.setScore(ats.getScore());

        response.setSkillScore(ats.getSkillScore());
        response.setKeywordScore(ats.getKeywordScore());
        response.setExperienceScore(ats.getExperienceScore());
        response.setEducationScore(ats.getEducationScore());
        response.setProjectScore(ats.getProjectScore());
        response.setCertificationScore(ats.getCertificationScore());
        response.setSectionScore(ats.getSectionScore());
        response.setFormattingScore(ats.getFormattingScore());

        response.setScoreLevel(ats.getScoreLevel());

        response.setMatchedSkills(ats.getMatchedSkills());
        response.setMissingSkills(ats.getMissingSkills());

        response.setStrengths(ats.getStrengths());
        response.setWeaknesses(ats.getWeaknesses());
        response.setSuggestions(ats.getSuggestions());

        log.info("Resume uploaded successfully.");

        return response;
    }

    // =====================================================
    // Resume History
    // =====================================================

    public ResumePageResponse getResumes(
        int page,
        int size,
        String search,
        String sort) {

    User currentUser = getCurrentUser();

    List<ResumeResponse> allResumes =
            resumeRepository.findByUserId(currentUser.getId())
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());

    // Normalize search
    String query = search == null
            ? ""
            : search.trim().toLowerCase();

    // Search by resume filename
    if (!query.isEmpty()) {
        allResumes = allResumes.stream()
                .filter(resume -> {

                    String fileName =
                            resume.getOriginalFileName();

                    return fileName != null
                            && fileName.toLowerCase()
                                    .contains(query);
                })
                .collect(Collectors.toList());
    }

    // Sorting
    Comparator<ResumeResponse> comparator;

    switch (sort == null ? "newest" : sort) {

        case "oldest":
            comparator = Comparator.comparing(
                    ResumeResponse::getUploadedAt,
                    Comparator.nullsLast(
                            Comparator.naturalOrder()
                    )
            );
            break;

        case "scoreHigh":
            comparator = Comparator.comparing(
                    ResumeResponse::getResumeScore,
                    Comparator.nullsLast(
                            Comparator.reverseOrder()
                    )
            );
            break;

        case "scoreLow":
            comparator = Comparator.comparing(
                    ResumeResponse::getResumeScore,
                    Comparator.nullsLast(
                            Comparator.naturalOrder()
                    )
            );
            break;

        case "newest":
        default:
            comparator = Comparator.comparing(
                    ResumeResponse::getUploadedAt,
                    Comparator.nullsLast(
                            Comparator.reverseOrder()
                    )
            );
            break;
    }

    allResumes.sort(comparator);

    // Pagination validation
    int safePage = Math.max(page, 0);
    int safeSize = Math.max(size, 1);

    int totalElements = allResumes.size();

    int totalPages =
            (int) Math.ceil(
                    (double) totalElements / safeSize
            );

    int fromIndex =
            Math.min(
                    safePage * safeSize,
                    totalElements
            );

    int toIndex =
            Math.min(
                    fromIndex + safeSize,
                    totalElements
            );

    List<ResumeResponse> pageContent =
            allResumes.subList(
                    fromIndex,
                    toIndex
            );

    ResumePageResponse response =
            new ResumePageResponse();

    response.setResumes(pageContent);
    response.setCurrentPage(safePage);
    response.setTotalPages(
            Math.max(totalPages, 1)
    );
    response.setTotalElements(totalElements);

    return response;
}
    // =====================================================
    // Delete Resume
    // =====================================================

    public void deleteResume(Long id) {

        User currentUser = getCurrentUser();

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found."));

        if (!resume.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Resume not found.");
        }

        deleteStoredFile(resume);

resumeAnalysisRepository.findByResumeId(id)
        .ifPresent(resumeAnalysisRepository::delete);

deleteStoredFile(resume);

resumeRepository.delete(resume);    }

    // =====================================================
    // Helpers
    // =====================================================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    private void createUploadDirectory() {

        try {

            Files.createDirectories(uploadPath);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Could not create upload directory.",
                    e);
        }
    }

    private String saveFile(MultipartFile file)
            throws IOException {

        String storedFileName =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path destination =
                uploadPath.resolve(storedFileName);

        Files.copy(
                file.getInputStream(),
                destination,
                StandardCopyOption.REPLACE_EXISTING);

        return storedFileName;
    }

    private Resume buildResume(
            MultipartFile file,
            String storedFileName) {

        Resume resume = new Resume();

        resume.setTitle(file.getOriginalFilename());
        resume.setOriginalFileName(file.getOriginalFilename());
        resume.setStoredFileName(storedFileName);
        resume.setFileType(file.getContentType());
        resume.setFileSize(file.getSize());

        resume.setStoragePath(
                uploadPath.resolve(storedFileName).toString());

        resume.setResumeScore(0);

        return resume;
    }

    private void deleteStoredFile(Resume resume) {

        try {

            Files.deleteIfExists(
                    uploadPath.resolve(
                            resume.getStoredFileName()));

        } catch (IOException ignored) {
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a resume.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Maximum allowed file size is 5 MB.");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid file.");
        }

        if (!FileUtils.isAllowedResume(fileName)) {
            throw new IllegalArgumentException(
                    "Only PDF, DOC and DOCX files are allowed.");
        }
    }

    private ResumeResponse map(Resume resume) {

        ResumeResponse response =
                new ResumeResponse();

        response.setId(resume.getId());
        response.setOriginalFileName(resume.getOriginalFileName());
        response.setStoredFileName(resume.getStoredFileName());
        response.setFileType(resume.getFileType());
        response.setFileSize(resume.getFileSize());
        response.setResumeScore(resume.getResumeScore());
        response.setUploadedAt(resume.getUploadedAt());

        response.setScore(resume.getResumeScore());

        response.setScoreLevel(
                resume.getResumeScore() >= 80
                        ? "Excellent"
                        : resume.getResumeScore() >= 60
                        ? "Good"
                        : "Needs Improvement");

        return response;
    }

    private void saveResumeAnalysis(
        Resume resume,
        ATSResponse ats) {

    ResumeAnalysis analysis =
            resumeAnalysisRepository
                    .findByResumeId(resume.getId())
                    .orElseGet(ResumeAnalysis::new);

    analysis.setResume(resume);

    analysis.setScore(ats.getScore());
    analysis.setSkillScore(ats.getSkillScore());
    analysis.setKeywordScore(ats.getKeywordScore());
    analysis.setExperienceScore(ats.getExperienceScore());
    analysis.setEducationScore(ats.getEducationScore());
    analysis.setProjectScore(ats.getProjectScore());
    analysis.setCertificationScore(ats.getCertificationScore());
    analysis.setSectionScore(ats.getSectionScore());
    analysis.setFormattingScore(ats.getFormattingScore());

    analysis.setScoreLevel(ats.getScoreLevel());

    analysis.setMatchedSkills(ats.getMatchedSkills());
    analysis.setMissingSkills(ats.getMissingSkills());

    analysis.setStrengths(ats.getStrengths());
    analysis.setWeaknesses(ats.getWeaknesses());
    analysis.setSuggestions(ats.getSuggestions());

    resumeAnalysisRepository.save(analysis);

    log.info(
            "ATS analysis saved successfully for resume ID: {}",
            resume.getId()
    );
}
@Transactional(readOnly = true)
public ResumeResponse getResumeAnalysis(Long resumeId) {

    User currentUser = getCurrentUser();

    Resume resume = resumeRepository.findById(resumeId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Resume not found."
                    ));

    // Security: user can only access their own resume
    if (resume.getUser() == null
            || resume.getUser().getId() == null
            || !resume.getUser()
                    .getId()
                    .equals(currentUser.getId())) {

        throw new ResourceNotFoundException(
                "Resume not found."
        );
    }

    ResumeAnalysis analysis =
            resumeAnalysisRepository.findByResumeId(resumeId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Resume analysis not found."
                            ));

    ResumeResponse response = new ResumeResponse();

    // =====================================================
    // Resume information
    // =====================================================

    response.setId(resume.getId());

    response.setOriginalFileName(
            resume.getOriginalFileName()
    );

    response.setStoredFileName(
            resume.getStoredFileName()
    );

    response.setFileType(
            resume.getFileType()
    );

    response.setFileSize(
            resume.getFileSize()
    );

    response.setResumeScore(
            resume.getResumeScore()
    );

    response.setUploadedAt(
            resume.getUploadedAt()
    );

    // =====================================================
    // ATS information
    // =====================================================

    response.setScore(
            analysis.getScore()
    );

    response.setSkillScore(
            analysis.getSkillScore()
    );

    response.setKeywordScore(
            analysis.getKeywordScore()
    );

    response.setExperienceScore(
            analysis.getExperienceScore()
    );

    response.setEducationScore(
            analysis.getEducationScore()
    );

    response.setProjectScore(
            analysis.getProjectScore()
    );

    response.setCertificationScore(
            analysis.getCertificationScore()
    );

    response.setSectionScore(
            analysis.getSectionScore()
    );

    response.setFormattingScore(
            analysis.getFormattingScore()
    );

    response.setScoreLevel(
            analysis.getScoreLevel()
    );

    // =====================================================
    // Copy Hibernate collections while session is active
    // =====================================================

    response.setMatchedSkills(
            analysis.getMatchedSkills() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(
                            analysis.getMatchedSkills()
                    )
    );

    response.setMissingSkills(
            analysis.getMissingSkills() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(
                            analysis.getMissingSkills()
                    )
    );

    response.setStrengths(
            analysis.getStrengths() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(
                            analysis.getStrengths()
                    )
    );

    response.setWeaknesses(
            analysis.getWeaknesses() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(
                            analysis.getWeaknesses()
                    )
    );

    response.setSuggestions(
            analysis.getSuggestions() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(
                            analysis.getSuggestions()
                    )
    );

    return response;
}


}