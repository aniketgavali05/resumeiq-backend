package com.resumeiq.backend.jobmatching.dto;

import java.util.List;

public class JobMatchResponse {

    private int matchScore;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    private List<String> recommendations;

    private String salaryEstimate;

    private List<JobRecommendation> suggestedJobs;

    public JobMatchResponse() {
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
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

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public String getSalaryEstimate() {
        return salaryEstimate;
    }

    public void setSalaryEstimate(String salaryEstimate) {
        this.salaryEstimate = salaryEstimate;
    }

    public List<JobRecommendation> getSuggestedJobs() {
        return suggestedJobs;
    }

    public void setSuggestedJobs(List<JobRecommendation> suggestedJobs) {
        this.suggestedJobs = suggestedJobs;
    }
}