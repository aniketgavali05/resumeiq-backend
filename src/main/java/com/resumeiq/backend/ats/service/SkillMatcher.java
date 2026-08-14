package com.resumeiq.backend.ats.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.resumeiq.backend.ats.constants.SkillsDatabase;

@Service
public class SkillMatcher {

    public List<String> getMatchedSkills(
            String resumeText,
            String jobDescription) {

        Set<String> matched = new HashSet<>();

        String resume = resumeText.toLowerCase();
        String jd = jobDescription.toLowerCase();

        for (String skill : SkillsDatabase.SKILLS) {

            String s = skill.toLowerCase();

            if (resume.contains(s) && jd.contains(s)) {
                matched.add(skill);
            }
        }

        return new ArrayList<>(matched);
    }

    public List<String> getMissingSkills(
            String resumeText,
            String jobDescription) {

        Set<String> missing = new HashSet<>();

        String resume = resumeText.toLowerCase();
        String jd = jobDescription.toLowerCase();

        for (String skill : SkillsDatabase.SKILLS) {

            String s = skill.toLowerCase();

            if (jd.contains(s) && !resume.contains(s)) {
                missing.add(skill);
            }
        }

        return new ArrayList<>(missing);
    }

    public List<String> getExtraSkills(
            String resumeText,
            String jobDescription) {

        Set<String> extra = new HashSet<>();

        String resume = resumeText.toLowerCase();
        String jd = jobDescription.toLowerCase();

        for (String skill : SkillsDatabase.SKILLS) {

            String s = skill.toLowerCase();

            if (resume.contains(s) && !jd.contains(s)) {
                extra.add(skill);
            }
        }

        return new ArrayList<>(extra);
    }

    public int calculateSkillScore(
            String resumeText,
            String jobDescription) {

        List<String> matched =
                getMatchedSkills(resumeText, jobDescription);

        List<String> missing =
                getMissingSkills(resumeText, jobDescription);

        int total = matched.size() + missing.size();

        if (total == 0) {
            return 100;
        }

        double score =
                ((double) matched.size() / total) * 100;

        return (int) Math.round(score);
    }

    public double getMatchPercentage(
            String resumeText,
            String jobDescription) {

        return calculateSkillScore(
                resumeText,
                jobDescription);
    }
}