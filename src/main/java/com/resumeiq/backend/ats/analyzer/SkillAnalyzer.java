package com.resumeiq.backend.ats.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.resumeiq.backend.ats.constants.SkillsDatabase;

@Service
public class SkillAnalyzer {

    /**
     * Returns ATS score for technical skills.
     */
    public int calculateSkillScore(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            return 0;
        }

        List<String> skills = extractSkills(resumeText);

        int totalSkills = skills.size();

        if (totalSkills >= 20)
            return 100;

        if (totalSkills >= 18)
            return 95;

        if (totalSkills >= 15)
            return 90;

        if (totalSkills >= 12)
            return 80;

        if (totalSkills >= 10)
            return 70;

        if (totalSkills >= 8)
            return 60;

        if (totalSkills >= 5)
            return 50;

        return 30;
    }

    /**
     * Returns all detected skills.
     */
    public List<String> extractSkills(String resumeText) {

        String text = resumeText.toLowerCase();

        Set<String> detected = new HashSet<>();

        for (String skill : SkillsDatabase.SKILLS) {

            if (text.contains(skill.toLowerCase())) {
                detected.add(skill);
            }
        }

        return new ArrayList<>(detected);
    }

    /**
     * Returns number of detected skills.
     */
    public int countSkills(String resumeText) {

        return extractSkills(resumeText).size();
    }

    /**
     * Checks whether a skill exists.
     */
    public boolean hasSkill(String resumeText, String skill) {

        if (resumeText == null || skill == null)
            return false;

        return resumeText.toLowerCase().contains(skill.toLowerCase());
    }

}