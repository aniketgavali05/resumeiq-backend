package com.resumeiq.backend.ats.dto;

import java.util.List;

public class ATSResponse {

    private int score;

    private int skillScore;
    private int keywordScore;
    private int experienceScore;
    private int educationScore;
    private int projectScore;
    private int certificationScore;
    private int sectionScore;
    private int formattingScore;

    private String scoreLevel;

    private List<String> matchedSkills;
    private List<String> missingSkills;

    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> suggestions;

    public ATSResponse() {
    }

    public ATSResponse(
            int score,
            int skillScore,
            int keywordScore,
            int experienceScore,
            int educationScore,
            int projectScore,
            int certificationScore,
            int sectionScore,
            int formattingScore,
            String scoreLevel,
            List<String> matchedSkills,
            List<String> missingSkills,
            List<String> strengths,
            List<String> weaknesses,
            List<String> suggestions) {

        this.score = score;
        this.skillScore = skillScore;
        this.keywordScore = keywordScore;
        this.experienceScore = experienceScore;
        this.educationScore = educationScore;
        this.projectScore = projectScore;
        this.certificationScore = certificationScore;
        this.sectionScore = sectionScore;
        this.formattingScore = formattingScore;

        this.scoreLevel = scoreLevel;

        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;

        this.strengths = strengths;
        this.weaknesses = weaknesses;
        this.suggestions = suggestions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(int skillScore) {
        this.skillScore = skillScore;
    }

    public int getKeywordScore() {
        return keywordScore;
    }

    public void setKeywordScore(int keywordScore) {
        this.keywordScore = keywordScore;
    }

    public int getExperienceScore() {
        return experienceScore;
    }

    public void setExperienceScore(int experienceScore) {
        this.experienceScore = experienceScore;
    }

    public int getEducationScore() {
        return educationScore;
    }

    public void setEducationScore(int educationScore) {
        this.educationScore = educationScore;
    }

    public int getProjectScore() {
        return projectScore;
    }

    public void setProjectScore(int projectScore) {
        this.projectScore = projectScore;
    }

    public int getCertificationScore() {
        return certificationScore;
    }

    public void setCertificationScore(int certificationScore) {
        this.certificationScore = certificationScore;
    }

    public int getSectionScore() {
        return sectionScore;
    }

    public void setSectionScore(int sectionScore) {
        this.sectionScore = sectionScore;
    }

    public int getFormattingScore() {
        return formattingScore;
    }

    public void setFormattingScore(int formattingScore) {
        this.formattingScore = formattingScore;
    }

    public String getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(String scoreLevel) {
        this.scoreLevel = scoreLevel;
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

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}