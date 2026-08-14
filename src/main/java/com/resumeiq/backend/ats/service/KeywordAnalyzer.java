package com.resumeiq.backend.ats.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class KeywordAnalyzer {

    /**
     * Common English stop words that should not affect ATS scoring.
     */
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(

            "the", "a", "an", "and", "or", "for", "to", "of",
            "in", "on", "at", "is", "are", "was", "were",
            "be", "been", "being", "this", "that", "these",
            "those", "with", "by", "from", "as", "it",
            "your", "our", "their", "his", "her", "its",
            "using", "use", "will", "can", "should"

    ));

    public int calculateKeywordScore(

            String resumeText,
            String jobDescription

    ) {

        if (resumeText == null || jobDescription == null) {
            return 0;
        }

        Set<String> jdKeywords = extractKeywords(jobDescription);

        if (jdKeywords.isEmpty()) {
            return 100;
        }

        String resume = resumeText.toLowerCase();

        int matched = 0;

        for (String keyword : jdKeywords) {

            if (resume.contains(keyword)) {
                matched++;
            }

        }

        double score =
                ((double) matched / jdKeywords.size()) * 100;

        return (int) Math.round(score);

    }

    /**
     * Extracts meaningful keywords from the Job Description.
     */
    public Set<String> extractKeywords(String text) {

        Set<String> keywords = new HashSet<>();

        if (text == null) {
            return keywords;
        }

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9 ]", " ");

        String[] words = cleaned.split("\\s+");

        for (String word : words) {

            if (word.length() < 3) {
                continue;
            }

            if (STOP_WORDS.contains(word)) {
                continue;
            }

            keywords.add(word);

        }

        return keywords;

    }

    /**
     * Number of important keywords in the Job Description.
     */
    public int totalKeywords(String jobDescription) {

        return extractKeywords(jobDescription).size();

    }

    /**
     * Number of keywords matched inside Resume.
     */
    public int matchedKeywords(

            String resumeText,
            String jobDescription

    ) {

        Set<String> keywords =
                extractKeywords(jobDescription);

        int matched = 0;

        String resume =
                resumeText.toLowerCase();

        for (String keyword : keywords) {

            if (resume.contains(keyword)) {
                matched++;
            }

        }

        return matched;

    }

}