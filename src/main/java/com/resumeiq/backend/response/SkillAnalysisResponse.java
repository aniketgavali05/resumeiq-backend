package com.resumeiq.backend.response;

import java.util.List;

public class SkillAnalysisResponse {

    private List<SkillItem> skills;

    public SkillAnalysisResponse() {
    }

    public SkillAnalysisResponse(
            List<SkillItem> skills
    ) {
        this.skills = skills;
    }

    public List<SkillItem> getSkills() {
        return skills;
    }

    public void setSkills(
            List<SkillItem> skills
    ) {
        this.skills = skills;
    }

    public static class SkillItem {

        private String skill;
        private int currentLevel;
        private int requiredLevel;
        private String category;

        public SkillItem() {
        }

        public SkillItem(
                String skill,
                int currentLevel,
                int requiredLevel,
                String category
        ) {
            this.skill = skill;
            this.currentLevel = currentLevel;
            this.requiredLevel = requiredLevel;
            this.category = category;
        }

        public String getSkill() {
            return skill;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }

        public String getCategory() {
            return category;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public void setCurrentLevel(
                int currentLevel
        ) {
            this.currentLevel = currentLevel;
        }

        public void setRequiredLevel(
                int requiredLevel
        ) {
            this.requiredLevel = requiredLevel;
        }

        public void setCategory(
                String category
        ) {
            this.category = category;
        }
    }
}