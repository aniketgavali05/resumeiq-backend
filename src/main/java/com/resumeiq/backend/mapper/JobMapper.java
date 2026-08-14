package com.resumeiq.backend.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.resumeiq.backend.entity.Job;
import com.resumeiq.backend.jobmatching.dto.MatchResult;

@Component
public class JobMapper {

    public MatchResult toMatchResult(
            Job job,
            int matchScore,
            int atsScore,
            List<String> matchedSkills,
            List<String> missingSkills) {

        if (job == null) {
            return null;
        }

        MatchResult result = new MatchResult();

        result.setJobId(job.getId());
        result.setJobTitle(job.getTitle());
        result.setCompany(job.getCompany());
        result.setLocation(job.getLocation());

        result.setMatchScore(matchScore);
        result.setAtsScore(atsScore);

        result.setMatchedSkills(matchedSkills);
        result.setMissingSkills(missingSkills);

        result.setRecommendation(getRecommendation(matchScore));

        return result;
    }

    private String getRecommendation(int score) {

        if (score >= 90) {
            return "Excellent Match";
        } else if (score >= 75) {
            return "Good Match";
        } else if (score >= 60) {
            return "Average Match";
        } else if (score >= 40) {
            return "Low Match";
        }

        return "Not Recommended";
    }
}