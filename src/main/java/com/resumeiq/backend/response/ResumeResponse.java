package com.resumeiq.backend.response;

import java.time.LocalDateTime;
import java.util.List;

public class ResumeResponse {

    private Long id;

    private String originalFileName;

    private String storedFileName;

    private String fileType;

    private Long fileSize;

    private Integer resumeScore;

    private LocalDateTime uploadedAt;

    // ATS

    private Integer score;

    private Integer skillScore;
    private Integer keywordScore;
    private Integer experienceScore;
    private Integer educationScore;
    private Integer projectScore;
    private Integer certificationScore;
    private Integer sectionScore;
    private Integer formattingScore;

    private String scoreLevel;

    private List<String> matchedSkills;
    private List<String> missingSkills;

    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> suggestions;

    public ResumeResponse() {
    }

    // ---------- getters setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(Integer resumeScore) {
        this.resumeScore = resumeScore;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
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
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

}