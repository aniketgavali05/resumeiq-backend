package com.resumeiq.backend.jobmatching.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class MatchCalculator {

    /**
     * Calculate experience compatibility.
     *
     * Returns:
     * 100 = candidate meets/exceeds requirement
     * 80  = close to requirement
     * 60  = some experience but below requirement
     * 30  = no detectable experience
     */
    public int calculateExperienceScore(
            String resumeText,
            String jobText) {

        int candidateYears =
                extractYears(resumeText);

        int requiredYears =
                extractRequiredYears(jobText);

        if (requiredYears <= 0) {

            return candidateYears > 0
                    ? 100
                    : 70;
        }

        if (candidateYears >= requiredYears) {
            return 100;
        }

        if (candidateYears >= requiredYears - 1) {
            return 80;
        }

        if (candidateYears > 0) {
            return 60;
        }

        return 30;
    }

    /**
     * Calculate education compatibility.
     */
    public int calculateEducationScore(
            String resumeText,
            String jobText) {

        String resume =
                safeLower(resumeText);

        String job =
                safeLower(jobText);

        if (job.contains("phd")
                && resume.contains("phd")) {
            return 100;
        }

        if ((job.contains("master")
                || job.contains("m.tech")
                || job.contains("mca")
                || job.contains("mba"))
                &&
                (resume.contains("master")
                        || resume.contains("m.tech")
                        || resume.contains("mca")
                        || resume.contains("mba"))) {

            return 100;
        }

        if ((job.contains("bachelor")
                || job.contains("b.tech")
                || job.contains("b.e")
                || job.contains("degree"))
                &&
                (resume.contains("bachelor")
                        || resume.contains("b.tech")
                        || resume.contains("b.e")
                        || resume.contains("bca")
                        || resume.contains("bsc")
                        || resume.contains("degree"))) {

            return 100;
        }

        /*
         * If the job does not specify an education requirement,
         * do not unfairly penalize the candidate.
         */
        boolean jobHasEducationRequirement =
                job.contains("bachelor")
                        || job.contains("b.tech")
                        || job.contains("b.e")
                        || job.contains("master")
                        || job.contains("m.tech")
                        || job.contains("mca")
                        || job.contains("mba")
                        || job.contains("phd")
                        || job.contains("degree");

        if (!jobHasEducationRequirement) {
            return 80;
        }

        return 50;
    }

    /**
     * Calculate the final weighted match score.
     *
     * Skills       = 40%
     * Keywords     = 20%
     * ATS score    = 20%
     * Experience   = 10%
     * Education    = 10%
     */
    public int calculateFinalScore(
            int skillScore,
            int keywordScore,
            int atsScore,
            int experienceScore,
            int educationScore) {

        double score =
                (skillScore * 0.40)
                        + (keywordScore * 0.20)
                        + (atsScore * 0.20)
                        + (experienceScore * 0.10)
                        + (educationScore * 0.10);

        return clamp((int) Math.round(score));
    }

    private int extractYears(String text) {

        if (text == null || text.isBlank()) {
            return 0;
        }

        Pattern pattern =
                Pattern.compile(
                        "(\\d+)\\+?\\s*(years|year|yrs|yr)",
                        Pattern.CASE_INSENSITIVE);

        Matcher matcher =
                pattern.matcher(text);

        int years = 0;

        while (matcher.find()) {

            try {

                years = Math.max(
                        years,
                        Integer.parseInt(
                                matcher.group(1)));

            } catch (NumberFormatException ignored) {
                // Ignore malformed values.
            }
        }

        return years;
    }

    private int extractRequiredYears(String text) {

        if (text == null || text.isBlank()) {
            return 0;
        }

        String lower =
                text.toLowerCase();

        Pattern pattern =
                Pattern.compile(
                        "(?:at least\\s+|minimum\\s+|min\\.?\\s*)?"
                                + "(\\d+)\\+?\\s*(?:years|year|yrs|yr)",
                        Pattern.CASE_INSENSITIVE);

        Matcher matcher =
                pattern.matcher(lower);

        int highest = 0;

        while (matcher.find()) {

            try {

                highest = Math.max(
                        highest,
                        Integer.parseInt(
                                matcher.group(1)));

            } catch (NumberFormatException ignored) {
                // Ignore malformed values.
            }
        }

        return highest;
    }

    private String safeLower(String value) {

        return value == null
                ? ""
                : value.toLowerCase();
    }

    private int clamp(int score) {

        return Math.max(
                0,
                Math.min(100, score));
    }
}