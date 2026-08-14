package com.resumeiq.backend.ats.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class AchievementAnalyzer {

    private static final String[] ACTION_VERBS = {

            "developed",
            "designed",
            "implemented",
            "created",
            "built",
            "optimized",
            "improved",
            "reduced",
            "increased",
            "led",
            "managed",
            "architected",
            "deployed",
            "delivered",
            "automated",
            "engineered",
            "migrated",
            "launched",
            "integrated",
            "resolved"

    };

    /**
     * Calculates achievement score.
     */
    public int calculateAchievementScore(String resumeText) {

        List<String> achievements = extractAchievements(resumeText);

        int total = achievements.size();

        if (total >= 12)
            return 100;

        if (total >= 10)
            return 95;

        if (total >= 8)
            return 90;

        if (total >= 6)
            return 80;

        if (total >= 4)
            return 70;

        if (total >= 2)
            return 60;

        return 40;
    }

    /**
     * Extract achievement sentences.
     */
    public List<String> extractAchievements(String resumeText) {

        List<String> list = new ArrayList<>();

        if (resumeText == null)
            return list;

        String[] lines = resumeText.split("\\n");

        Pattern numberPattern = Pattern.compile(
                "(\\d+%|\\d+\\+?|\\$\\d+|\\d+ms|\\d+ million)");

        for (String line : lines) {

            String lower = line.toLowerCase();

            boolean action = false;

            for (String verb : ACTION_VERBS) {

                if (lower.contains(verb)) {

                    action = true;
                    break;

                }

            }

            Matcher matcher = numberPattern.matcher(lower);

            if (action && matcher.find()) {

                list.add(line.trim());

            }

        }

        return list;
    }

    /**
     * Number of achievements.
     */
    public int totalAchievements(String resumeText) {

        return extractAchievements(resumeText).size();

    }

}