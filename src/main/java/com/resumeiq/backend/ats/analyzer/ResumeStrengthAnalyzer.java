package com.resumeiq.backend.ats.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ResumeStrengthAnalyzer {

    public List<String> analyzeStrengths(

            int skillScore,
            int keywordScore,
            int experienceScore,
            int educationScore,
            int projectScore,
            int certificationScore,
            int sectionScore,
            int formattingScore,
            int achievementScore,
            int actionVerbScore,
            int grammarScore,
            int readabilityScore

    ) {

        List<String> strengths = new ArrayList<>();

        if (skillScore >= 80)
            strengths.add("Strong technical skills.");

        if (keywordScore >= 80)
            strengths.add("Excellent keyword matching with the Job Description.");

        if (experienceScore >= 80)
            strengths.add("Good professional experience.");

        if (educationScore >= 80)
            strengths.add("Strong educational background.");

        if (projectScore >= 80)
            strengths.add("High-quality industry-level projects.");

        if (certificationScore >= 80)
            strengths.add("Professional certifications strengthen your profile.");

        if (sectionScore >= 90)
            strengths.add("Resume contains all important ATS sections.");

        if (formattingScore >= 90)
            strengths.add("Clean and ATS-friendly formatting.");

        if (achievementScore >= 80)
            strengths.add("Achievements are measurable and impactful.");

        if (actionVerbScore >= 80)
            strengths.add("Excellent use of action verbs.");

        if (grammarScore >= 90)
            strengths.add("Excellent grammar and writing quality.");

        if (readabilityScore >= 90)
            strengths.add("Easy to read and professionally written.");

        if (strengths.isEmpty()) {
            strengths.add("No major strengths detected yet.");
        }

        return strengths;
    }

}