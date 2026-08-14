package com.resumeiq.backend.response;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;

    private Long jobId;

    private String jobTitle;

    private String company;

    private String location;

    private String employmentType;

    private String experienceLevel;

    private String salaryRange;

    private String applyUrl;

    private String status;

    private String notes;

    private LocalDateTime appliedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ApplicationResponse() {
    }

    // =========================
    // Getters
    // =========================

    public Long getId() {
        return id;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // =========================
    // Setters
    // =========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}