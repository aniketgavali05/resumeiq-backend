package com.resumeiq.backend.ats.analyzer;

import org.springframework.stereotype.Service;

@Service
public class CertificationAnalyzer {

    public int calculateCertificationScore(String resumeText) {

        String text = resumeText.toLowerCase();

        int score = 0;

        if (text.contains("aws certified"))
            score += 25;

        if (text.contains("oracle"))
            score += 20;

        if (text.contains("azure"))
            score += 20;

        if (text.contains("google cloud"))
            score += 20;

        if (text.contains("salesforce"))
            score += 15;

        return Math.min(score, 100);
    }
}