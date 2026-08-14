package com.resumeiq.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resume_analysis")
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "resume_id",
        nullable = false,
        unique = true
    )
    private Resume resume;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Integer skillScore;

    @Column(nullable = false)
    private Integer keywordScore;

    @Column(nullable = false)
    private Integer experienceScore;

    @Column(nullable = false)
    private Integer educationScore;

    @Column(nullable = false)
    private Integer projectScore;

    @Column(nullable = false)
    private Integer certificationScore;

    @Column(nullable = false)
    private Integer sectionScore;

    @Column(nullable = false)
    private Integer formattingScore;

    @Column(length = 50)
    private String scoreLevel;

    @ElementCollection
    @CollectionTable(
        name = "resume_analysis_matched_skills",
        joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "skill")
    private List<String> matchedSkills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "resume_analysis_missing_skills",
        joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "skill")
    private List<String> missingSkills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "resume_analysis_strengths",
        joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "strength")
    private List<String> strengths = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "resume_analysis_weaknesses",
        joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "weakness")
    private List<String> weaknesses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "resume_analysis_suggestions",
        joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "suggestion")
    private List<String> suggestions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ResumeAnalysis() {
    }

    // =========================
    // Lifecycle
    // =========================

    @jakarta.persistence.PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;
    }

    @jakarta.persistence.PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(Integer skillScore) {
        this.skillScore = skillScore;
    }

    public Integer getKeywordScore() {
        return keywordScore;
    }

    public void setKeywordScore(Integer keywordScore) {
        this.keywordScore = keywordScore;
    }

    public Integer getExperienceScore() {
        return experienceScore;
    }

    public void setExperienceScore(Integer experienceScore) {
        this.experienceScore = experienceScore;
    }

    public Integer getEducationScore() {
        return educationScore;
    }

    public void setEducationScore(Integer educationScore) {
        this.educationScore = educationScore;
    }

    public Integer getProjectScore() {
        return projectScore;
    }

    public void setProjectScore(Integer projectScore) {
        this.projectScore = projectScore;
    }

    public Integer getCertificationScore() {
        return certificationScore;
    }

    public void setCertificationScore(Integer certificationScore) {
        this.certificationScore = certificationScore;
    }

    public Integer getSectionScore() {
        return sectionScore;
    }

    public void setSectionScore(Integer sectionScore) {
        this.sectionScore = sectionScore;
    }

    public Integer getFormattingScore() {
        return formattingScore;
    }

    public void setFormattingScore(Integer formattingScore) {
        this.formattingScore = formattingScore;
    }

    public String getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(String scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills != null
            ? new ArrayList<>(matchedSkills)
            : new ArrayList<>();
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills != null
            ? new ArrayList<>(missingSkills)
            : new ArrayList<>();
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths != null
            ? new ArrayList<>(strengths)
            : new ArrayList<>();
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses != null
            ? new ArrayList<>(weaknesses)
            : new ArrayList<>();
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions != null
            ? new ArrayList<>(suggestions)
            : new ArrayList<>();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}