package com.resumeiq.backend.ats.analyzer;

import org.springframework.stereotype.Service;

@Service
public class ProjectAnalyzer {

    public int calculateProjectScore(String resumeText) {

        String text = resumeText.toLowerCase();

        int score = 0;

        if (text.contains("project"))
            score += 20;

        if (text.contains("github"))
            score += 20;

        if (text.contains("spring"))
            score += 15;

        if (text.contains("spring boot"))
            score += 15;

        if (text.contains("react"))
            score += 10;

        if (text.contains("next"))
            score += 10;

        if (text.contains("docker"))
            score += 5;

        if (text.contains("aws"))
            score += 5;

        return Math.min(score, 100);
    }
}