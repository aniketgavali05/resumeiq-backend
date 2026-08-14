package com.resumeiq.backend.ats.dto;

import java.util.List;

public class SkillResult {

    private List<String> matchedSkills;
    private List<String> missingSkills;

    public SkillResult() {
    }

    public SkillResult(List<String> matchedSkills,
                       List<String> missingSkills) {
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
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
}