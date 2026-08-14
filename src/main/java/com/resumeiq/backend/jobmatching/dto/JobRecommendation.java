package com.resumeiq.backend.jobmatching.dto;

public class JobRecommendation {

    private String title;
    private String company;
    private String location;
    private String salary;
    private int matchPercentage;

    public JobRecommendation() {
    }

    public JobRecommendation(
            String title,
            String company,
            String location,
            String salary,
            int matchPercentage) {

        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.matchPercentage = matchPercentage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }
}