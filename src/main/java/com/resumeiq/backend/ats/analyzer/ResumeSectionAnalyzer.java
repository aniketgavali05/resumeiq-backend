package com.resumeiq.backend.ats.analyzer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ResumeSectionAnalyzer {

    public Map<String, Boolean> analyzeSections(String resumeText) {

        String text = resumeText.toLowerCase();

        Map<String, Boolean> sections = new LinkedHashMap<>();

        sections.put("Contact Information",
                containsAny(text,
                        "email",
                        "@",
                        "phone",
                        "mobile"));

        sections.put("Professional Summary",
                containsAny(text,
                        "summary",
                        "profile",
                        "objective",
                        "career objective"));

        sections.put("Skills",
                containsAny(text,
                        "skills",
                        "technical skills",
                        "technologies"));

        sections.put("Experience",
                containsAny(text,
                        "experience",
                        "work experience",
                        "employment"));

        sections.put("Education",
                containsAny(text,
                        "education",
                        "academic"));

        sections.put("Projects",
                containsAny(text,
                        "project",
                        "projects"));

        sections.put("Certifications",
                containsAny(text,
                        "certification",
                        "certifications",
                        "certificate"));

        sections.put("Achievements",
                containsAny(text,
                        "achievement",
                        "awards",
                        "honors"));

        sections.put("Languages",
                containsAny(text,
                        "languages",
                        "language"));

        return sections;
    }

    public int calculateSectionScore(String resumeText) {

        Map<String, Boolean> sections =
                analyzeSections(resumeText);

        int total = sections.size();

        int present = 0;

        for (boolean value : sections.values()) {

            if (value) {
                present++;
            }
        }

        return (int) Math.round(
                (present * 100.0) / total);
    }

    private boolean containsAny(String text,
                                String... keywords) {

        for (String keyword : keywords) {

            if (text.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}