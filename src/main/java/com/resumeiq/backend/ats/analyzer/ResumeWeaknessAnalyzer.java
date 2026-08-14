package com.resumeiq.backend.ats.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ResumeWeaknessAnalyzer {

    public List<String> analyzeWeaknesses(

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

        List<String> weaknesses = new ArrayList<>();

        if (skillScore < 60)
            weaknesses.add("Technical skills need improvement.");

        if (keywordScore < 70)
            weaknesses.add("Resume is missing important job keywords.");

        if (experienceScore < 60)
            weaknesses.add("Professional experience is limited or missing.");

        if (educationScore < 60)
            weaknesses.add("Education details are incomplete.");

        if (projectScore < 70)
            weaknesses.add("Add more real-world projects.");

        if (certificationScore < 60)
            weaknesses.add("Professional certifications are missing.");

        if (sectionScore < 80)
            weaknesses.add("Resume is missing important ATS sections.");

        if (formattingScore < 80)
            weaknesses.add("Resume formatting should be improved.");

        if (achievementScore < 70)
            weaknesses.add("Add measurable achievements with numbers and results.");

        if (actionVerbScore < 70)
            weaknesses.add("Use stronger action verbs such as Developed, Designed, Led, Implemented.");

        if (grammarScore < 80)
            weaknesses.add("Grammar and writing quality should be improved.");

        if (readabilityScore < 80)
            weaknesses.add("Improve readability by using shorter sentences.");

        if (weaknesses.isEmpty()) {
            weaknesses.add("No major weaknesses detected.");
        }

        return weaknesses;
    }

}