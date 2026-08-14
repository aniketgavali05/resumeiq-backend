package com.resumeiq.backend.ats.analyzer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class GrammarAnalyzer {

    private static final Set<String> WEAK_WORDS = new HashSet<>(Arrays.asList(

            "very",
            "really",
            "good",
            "nice",
            "helped",
            "worked",
            "responsible",
            "various",
            "etc",
            "things",
            "stuff"

    ));

    public int calculateGrammarScore(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            return 0;
        }

        int score = 100;

        String[] words = resumeText.split("\\s+");

        if (words.length < 150)
            score -= 10;

        if (words.length > 1000)
            score -= 5;

        int weakCount = 0;

        String lower = resumeText.toLowerCase();

        for (String word : WEAK_WORDS) {

            if (lower.contains(word)) {
                weakCount++;
            }

        }

        score -= weakCount * 2;

        if (resumeText.contains(".."))
            score -= 5;

        if (resumeText.contains(",,"))
            score -= 5;

        if (resumeText.contains("!!"))
            score -= 5;

        if (score < 0)
            score = 0;

        if (score > 100)
            score = 100;

        return score;
    }

    public int calculateReadabilityScore(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            return 0;
        }

        String[] sentences = resumeText.split("[.!?]");

        String[] words = resumeText.split("\\s+");

        if (sentences.length == 0)
            return 100;

        double averageWords =
                (double) words.length / sentences.length;

        if (averageWords <= 18)
            return 100;

        if (averageWords <= 22)
            return 90;

        if (averageWords <= 26)
            return 80;

        if (averageWords <= 30)
            return 70;

        return 60;
    }

    public String getGrammarLevel(int score) {

        if (score >= 90)
            return "Excellent";

        if (score >= 80)
            return "Very Good";

        if (score >= 70)
            return "Good";

        if (score >= 60)
            return "Average";

        return "Needs Improvement";
    }

}