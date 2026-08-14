package com.resumeiq.backend.response;

public class DashboardResponse {

    private long totalApplications;
    private long interviews;
    private long offers;
    private int avgMatchScore;

    public DashboardResponse() {
    }

    public DashboardResponse(
            long totalApplications,
            long interviews,
            long offers,
            int avgMatchScore
    ) {
        this.totalApplications = totalApplications;
        this.interviews = interviews;
        this.offers = offers;
        this.avgMatchScore = avgMatchScore;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getInterviews() {
        return interviews;
    }

    public void setInterviews(long interviews) {
        this.interviews = interviews;
    }

    public long getOffers() {
        return offers;
    }

    public void setOffers(long offers) {
        this.offers = offers;
    }

    public int getAvgMatchScore() {
        return avgMatchScore;
    }

    public void setAvgMatchScore(int avgMatchScore) {
        this.avgMatchScore = avgMatchScore;
    }
}