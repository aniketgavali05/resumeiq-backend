package com.resumeiq.backend.ats.analyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ExperienceAnalyzer {

    public int calculateExperienceScore(String resumeText) {

        Pattern pattern = Pattern.compile(
                "(\\d+)\\+?\\s*(years|year|yrs|yr)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(resumeText);

        int years = 0;

        while (matcher.find()) {

            years = Math.max(
                    years,
                    Integer.parseInt(matcher.group(1)));
        }

        if (years >= 8)
            return 100;

        if (years >= 6)
            return 95;

        if (years >= 4)
            return 85;

        if (years >= 3)
            return 75;

        if (years >= 2)
            return 65;

        if (years >= 1)
            return 50;

        return 20;
    }
}