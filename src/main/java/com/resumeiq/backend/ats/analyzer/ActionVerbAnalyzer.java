package com.resumeiq.backend.ats.analyzer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ActionVerbAnalyzer {

    private static final Set<String> STRONG_VERBS = new HashSet<>(Arrays.asList(

            "developed",
            "designed",
            "implemented",
            "created",
            "built",
            "optimized",
            "improved",
            "managed",
            "led",
            "architected",
            "automated",
            "deployed",
            "engineered",
            "integrated",
            "launched",
            "migrated",
            "resolved",
            "achieved",
            "delivered",
            "reduced",
            "increased"

    ));

    public int calculateActionVerbScore(String resumeText) {

        if (resumeText == null)
            return 0;

        int count = 0;

        String lower = resumeText.toLowerCase();

        for (String verb : STRONG_VERBS) {

            if (lower.contains(verb)) {
                count++;
            }

        }

        if (count >= 15)
            return 100;

        if (count >= 12)
            return 95;

        if (count >= 10)
            return 90;

        if (count >= 8)
            return 80;

        if (count >= 6)
            return 70;

        if (count >= 4)
            return 60;

        return 40;
    }

}