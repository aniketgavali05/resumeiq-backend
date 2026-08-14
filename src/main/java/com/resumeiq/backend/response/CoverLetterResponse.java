package com.resumeiq.backend.response;

import java.time.LocalDateTime;

public class CoverLetterResponse {

    private Long id;
    private Long applicationId;

    private String jobTitle;
    private String company;
    private String tone;
    private String content;
    private LocalDateTime createdAt;

    public CoverLetterResponse() {
    }

    public CoverLetterResponse(
            Long id,
            Long applicationId,
            String jobTitle,
            String company,
            String tone,
            String content,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.jobTitle = jobTitle;
        this.company = company;
        this.tone = tone;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public String getTone() {
        return tone;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}