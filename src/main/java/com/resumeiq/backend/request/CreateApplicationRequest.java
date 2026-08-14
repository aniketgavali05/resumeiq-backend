package com.resumeiq.backend.request;

import jakarta.validation.constraints.NotNull;

public class CreateApplicationRequest {

    @NotNull(message = "Job ID is required.")
    private Long jobId;

    private String notes;

    public CreateApplicationRequest() {
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}