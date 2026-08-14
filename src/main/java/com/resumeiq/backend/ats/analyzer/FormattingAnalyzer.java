package com.resumeiq.backend.ats.analyzer;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class FormattingAnalyzer {

    public int calculateFormattingScore(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            return 0;
        }

        int score = 100;

        String text = resumeText.trim();

        // Resume Length
        int words = text.split("\\s+").length;

        if (words < 200)
            score -= 20;

        if (words > 1200)
            score -= 15;

        // Email
        if (!containsEmail(text))
            score -= 15;

        // Phone
        if (!containsPhone(text))
            score -= 15;

        // Bullet Points
        if (!containsBulletPoints(text))
            score -= 10;

        // Section Headings
        if (!containsSections(text))
            score -= 15;

        // Long Paragraphs
        if (hasVeryLongParagraph(text))
            score -= 10;

        // Multiple Empty Lines
        if (text.contains("\n\n\n"))
            score -= 5;

        return Math.max(score, 0);
    }

    private boolean containsEmail(String text) {

        return Pattern.compile(
                "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+")
                .matcher(text)
                .find();
    }

    private boolean containsPhone(String text) {

        return Pattern.compile(
                "(\\+\\d{1,3}[\\s-]?)?(\\d{10})")
                .matcher(text)
                .find();
    }

    private boolean containsBulletPoints(String text) {

        return text.contains("•")
                || text.contains("- ")
                || text.contains("* ");
    }

    private boolean containsSections(String text) {

        String lower = text.toLowerCase();

        return lower.contains("education")
                && lower.contains("experience")
                && lower.contains("skills");
    }

    private boolean hasVeryLongParagraph(String text) {

        String[] paragraphs = text.split("\n");

        for (String p : paragraphs) {

            if (p.split("\\s+").length > 120) {
                return true;
            }
        }

        return false;
    }
}