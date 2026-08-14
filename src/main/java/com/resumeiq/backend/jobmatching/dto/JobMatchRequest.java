package com.resumeiq.backend.jobmatching.dto;

public class JobMatchRequest {

    private String resumeText;
    private String jobDescription;

    public JobMatchRequest() {
    }

    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}