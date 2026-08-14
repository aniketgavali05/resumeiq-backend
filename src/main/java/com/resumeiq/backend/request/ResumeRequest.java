package com.resumeiq.backend.request;

public class ResumeRequest {

    private Long resumeId;

    private String jobDescription;

    public ResumeRequest() {
    }

    public ResumeRequest(Long resumeId, String jobDescription) {
        this.resumeId = resumeId;
        this.jobDescription = jobDescription;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

}