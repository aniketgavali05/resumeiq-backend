package com.resumeiq.backend.response;

public class JobResponse {

    private Long id;
    private String company;
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private String experienceLevel;
    private String salaryRange;
    private String applyUrl;
    private Boolean active;

    public JobResponse() {
    }

    public JobResponse(
            Long id,
            String company,
            String title,
            String description,
            String location,
            String employmentType,
            String experienceLevel,
            String salaryRange,
            String applyUrl,
            Boolean active) {

        this.id = id;
        this.company = company;
        this.title = title;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.experienceLevel = experienceLevel;
        this.salaryRange = salaryRange;
        this.applyUrl = applyUrl;
        this.active = active;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public Boolean getActive() {
        return active;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}