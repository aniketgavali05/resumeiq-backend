package com.resumeiq.backend.response;

import java.time.LocalDateTime;

public class InterviewResponse {

    private Long id;
    private Long applicationId;

    private Long jobId;
    private String jobTitle;
    private String company;

    private String interviewType;
    private LocalDateTime scheduledAt;

    private String interviewerName;
    private String meetingLink;
    private String location;

    private String notes;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InterviewResponse() {
    }

    public InterviewResponse(
            Long id,
            Long applicationId,
            Long jobId,
            String jobTitle,
            String company,
            String interviewType,
            LocalDateTime scheduledAt,
            String interviewerName,
            String meetingLink,
            String location,
            String notes,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.company = company;
        this.interviewType = interviewType;
        this.scheduledAt = scheduledAt;
        this.interviewerName = interviewerName;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getApplicationId() {
        return applicationId;
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

    public String getInterviewType() {
        return interviewType;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}