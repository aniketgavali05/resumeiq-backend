package com.resumeiq.backend.resume.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.resume.dto.ResumeAnalysisResult;
import com.resumeiq.backend.resume.util.ResumeRegexUtil;

@Service
public class ResumeAnalyzer {

    private final ResumeParsingService parsingService;

    public ResumeAnalyzer(ResumeParsingService parsingService) {
        this.parsingService = parsingService;
    }

    public ResumeAnalysisResult analyze(MultipartFile file) throws IOException {

        String text = parsingService.extractResumeText(file);

        ResumeAnalysisResult result = new ResumeAnalysisResult();

        result.setName(ResumeRegexUtil.extractName(text));
        result.setEmail(ResumeRegexUtil.extractEmail(text));
        result.setPhone(ResumeRegexUtil.extractPhone(text));

        List<String> skills = extractSkills(text);
        result.setSkills(skills);

        int atsScore = calculateAtsScore(skills);
        result.setAtsScore(atsScore);

        result.setSuggestions(generateSuggestions(atsScore, skills));

        return result;
    }

    private List<String> extractSkills(String text) {

        List<String> skills = new ArrayList<>();

        if (text == null) {
            return skills;
        }

        String lower = text.toLowerCase();

        addSkill(lower, skills, "java");
        addSkill(lower, skills, "spring");
        addSkill(lower, skills, "spring boot");
        addSkill(lower, skills, "react");
        addSkill(lower, skills, "angular");
        addSkill(lower, skills, "javascript");
        addSkill(lower, skills, "typescript");
        addSkill(lower, skills, "html");
        addSkill(lower, skills, "css");
        addSkill(lower, skills, "sql");
        addSkill(lower, skills, "mysql");
        addSkill(lower, skills, "postgresql");
        addSkill(lower, skills, "mongodb");
        addSkill(lower, skills, "docker");
        addSkill(lower, skills, "aws");
        addSkill(lower, skills, "git");
        addSkill(lower, skills, "hibernate");

        return skills;
    }

    private void addSkill(String text, List<String> skills, String keyword) {

        if (text.contains(keyword)) {
            skills.add(keyword);
        }
    }

    private int calculateAtsScore(List<String> skills) {

        int score = 40 + (skills.size() * 4);

        return Math.min(score, 100);
    }

    private List<String> generateSuggestions(
            int score,
            List<String> skills) {

        List<String> suggestions = new ArrayList<>();

        if (score < 70) {
            suggestions.add("Add more technical skills.");
            suggestions.add("Include measurable achievements.");
            suggestions.add("Improve project descriptions.");
        }

        if (!skills.contains("spring boot")) {
            suggestions.add("Mention Spring Boot experience.");
        }

        if (!skills.contains("docker")) {
            suggestions.add("Add Docker knowledge if applicable.");
        }

        if (!skills.contains("aws")) {
            suggestions.add("Include cloud technologies.");
        }

        return suggestions;
    }
}