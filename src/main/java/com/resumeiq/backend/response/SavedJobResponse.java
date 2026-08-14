package com.resumeiq.backend.response;

import java.time.LocalDateTime;

public class SavedJobResponse {

    private Long id;
    private Long jobId;

    private String title;
    private String company;
    private String location;
    private String description;
    private String employmentType;
    private String experienceLevel;
    private String salaryRange;
    private String applyUrl;
    private Boolean active;

    private LocalDateTime createdAt;

    public SavedJobResponse() {
    }

    public SavedJobResponse(
            Long id,
            Long jobId,
            String title,
            String company,
            String location,
            String description,
            String employmentType,
            String experienceLevel,
            String salaryRange,
            String applyUrl,
            Boolean active,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.description = description;
        this.employmentType = employmentType;
        this.experienceLevel = experienceLevel;
        this.salaryRange = salaryRange;
        this.applyUrl = applyUrl;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
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

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}