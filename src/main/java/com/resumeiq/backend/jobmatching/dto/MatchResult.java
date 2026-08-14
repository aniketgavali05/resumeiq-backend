package com.resumeiq.backend.jobmatching.dto;
import java.util.List;


public class MatchResult {

    private Long jobId;

    private String jobTitle;

    private String company;

    private String location;

    private Integer matchScore;

    private Integer atsScore;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    private String recommendation;

    public MatchResult() {
    }

    public MatchResult(
            Long jobId,
            String jobTitle,
            String company,
            String location,
            Integer matchScore,
            Integer atsScore,
            List<String> matchedSkills,
            List<String> missingSkills,
            String recommendation) {

        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.company = company;
        this.location = location;
        this.matchScore = matchScore;
        this.atsScore = atsScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.recommendation = recommendation;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
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

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}