package com.resumeiq.backend.response;

import java.util.List;

public class DashboardStatsResponse {

    private int applications;
    private int interviews;
    private int offers;
    private int savedJobs;

    private int resumeScore;
    private int matchPercentage;

    private List<DashboardRecentResumeResponse> recentResumes;

    public DashboardStatsResponse() {
    }

    public DashboardStatsResponse(
            int applications,
            int interviews,
            int offers,
            int savedJobs,
            int resumeScore,
            int matchPercentage,
            List<DashboardRecentResumeResponse> recentResumes) {

        this.applications = applications;
        this.interviews = interviews;
        this.offers = offers;
        this.savedJobs = savedJobs;
        this.resumeScore = resumeScore;
        this.matchPercentage = matchPercentage;
        this.recentResumes = recentResumes;
    }

    public int getApplications() {
        return applications;
    }

    public void setApplications(int applications) {
        this.applications = applications;
    }

    public int getInterviews() {
        return interviews;
    }

    public void setInterviews(int interviews) {
        this.interviews = interviews;
    }

    public int getOffers() {
        return offers;
    }

    public void setOffers(int offers) {
        this.offers = offers;
    }

    public int getSavedJobs() {
        return savedJobs;
    }

    public void setSavedJobs(int savedJobs) {
        this.savedJobs = savedJobs;
    }

    public int getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(int resumeScore) {
        this.resumeScore = resumeScore;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<DashboardRecentResumeResponse> getRecentResumes() {
        return recentResumes;
    }

    public void setRecentResumes(
            List<DashboardRecentResumeResponse> recentResumes) {
        this.recentResumes = recentResumes;
    }
}