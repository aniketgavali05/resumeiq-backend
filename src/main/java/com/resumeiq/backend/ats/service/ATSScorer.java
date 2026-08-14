package com.resumeiq.backend.ats.service;

import org.springframework.stereotype.Service;

@Service
public class ATSScorer {

    public int calculateFinalScore(

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

        double finalScore =

                (skillScore * 0.20) +

                (keywordScore * 0.15) +

                (experienceScore * 0.10) +

                (educationScore * 0.08) +

                (projectScore * 0.08) +

                (certificationScore * 0.05) +

                (sectionScore * 0.08) +

                (formattingScore * 0.08) +

                (achievementScore * 0.08) +

                (actionVerbScore * 0.05) +

                (grammarScore * 0.08) +

                (readabilityScore * 0.07);

        int score = (int) Math.round(finalScore);

        if (score > 100)
            score = 100;

        if (score < 0)
            score = 0;

        return score;
    }

    public String getScoreLevel(int score) {

        if (score >= 90)
            return "Outstanding";

        if (score >= 80)
            return "Excellent";

        if (score >= 70)
            return "Very Good";

        if (score >= 60)
            return "Good";

        if (score >= 50)
            return "Average";

        return "Needs Improvement";
    }

}