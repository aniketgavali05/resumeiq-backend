package com.resumeiq.backend.ats.analyzer;

import org.springframework.stereotype.Service;

@Service
public class EducationAnalyzer {

    public int calculateEducationScore(String resumeText) {

        String text = resumeText.toLowerCase();

        if (text.contains("phd"))
            return 100;

        if (text.contains("master")
                || text.contains("m.tech")
                || text.contains("mca")
                || text.contains("mba"))
            return 95;

        if (text.contains("bachelor")
                || text.contains("b.tech")
                || text.contains("b.e")
                || text.contains("be")
                || text.contains("bca")
                || text.contains("bsc"))
            return 85;

        if (text.contains("diploma"))
            return 65;

        return 30;
    }
}