package com.resumeiq.backend.resume.dto;

public class ResumeSummary {

    private Long totalResumes;

    private Integer highestScore;

    private Integer averageScore;

    public ResumeSummary() {
    }

    public ResumeSummary(Long totalResumes,
                         Integer highestScore,
                         Integer averageScore) {
        this.totalResumes = totalResumes;
        this.highestScore = highestScore;
        this.averageScore = averageScore;
    }

    public Long getTotalResumes() {
        return totalResumes;
    }

    public Integer getHighestScore() {
        return highestScore;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setTotalResumes(Long totalResumes) {
        this.totalResumes = totalResumes;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }
}