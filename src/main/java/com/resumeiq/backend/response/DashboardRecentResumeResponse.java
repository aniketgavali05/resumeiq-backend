package com.resumeiq.backend.response;

import java.time.LocalDateTime;

public class DashboardRecentResumeResponse {

    private Long id;
    private String fileName;
    private Integer score;
    private LocalDateTime uploadedAt;

    public DashboardRecentResumeResponse() {
    }

    public DashboardRecentResumeResponse(
            Long id,
            String fileName,
            Integer score,
            LocalDateTime uploadedAt) {

        this.id = id;
        this.fileName = fileName;
        this.score = score;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}