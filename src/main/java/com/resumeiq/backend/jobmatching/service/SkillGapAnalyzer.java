package com.resumeiq.backend.jobmatching.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillGapAnalyzer {

    public List<String> buildRecommendations(
            List<String> missingSkills) {

        List<String> recommendations = new ArrayList<>();

        for (String skill : missingSkills) {

            recommendations.add(
                    "Learn " + skill + " and add a project using it.");
        }

        return recommendations;
    }

}