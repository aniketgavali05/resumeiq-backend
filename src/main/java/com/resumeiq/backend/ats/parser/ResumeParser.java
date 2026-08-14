package com.resumeiq.backend.ats.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.resumeiq.backend.ats.constants.SkillsDatabase;
import com.resumeiq.backend.ats.dto.ResumeData;

@Service
public class ResumeParser {

    public ResumeData parse(String resumeText) {

        if (resumeText == null) {
            resumeText = "";
        }

        String text = resumeText.replace("\r", "").trim();

        ResumeData data = new ResumeData();

        data.setResume(isResume(text));
        data.setName(extractName(text));
        data.setEmail(extractEmail(text));
        data.setPhone(extractPhone(text));
        data.setLinkedin(extractLinkedIn(text));
        data.setGithub(extractGithub(text));
        data.setPortfolio(extractPortfolio(text));
        data.setSkills(extractSkills(text));
        data.setExperienceYears(extractExperience(text));

        if (containsEducation(text)) {
            data.getEducation().add("Education Section Found");
        }

        if (containsProjects(text)) {
            data.getProjects().add("Projects Section Found");
        }

        if (containsCertification(text)) {
            data.getCertifications().add("Certification Section Found");
        }

        return data;
    }

    private String extractName(String text) {

        String[] lines = text.split("\\n");

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty())
                continue;

            if (line.length() > 50)
                continue;

            if (line.contains("@"))
                continue;

            if (line.matches(".*\\d.*"))
                continue;

            return line;
        }

        return "";
    }

    public boolean isResume(String text) {

        String lower = text.toLowerCase();

        int score = 0;

        if (lower.contains("education"))
            score++;

        if (lower.contains("experience"))
            score++;

        if (lower.contains("skills"))
            score++;

        if (lower.contains("project"))
            score++;

        if (lower.contains("certification"))
            score++;

        if (extractEmail(text) != null)
            score++;

        if (extractPhone(text) != null)
            score++;

        return score >= 4;
    }

    public String extractEmail(String text) {

        Pattern pattern = Pattern.compile(
                "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    public String extractPhone(String text) {

        Pattern pattern = Pattern.compile(
                "(\\+\\d{1,3}[\\s-]?)?(\\d{10})");

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    public String extractLinkedIn(String text) {

        Pattern pattern = Pattern.compile(
                "(https?://)?(www\\.)?linkedin\\.com/in/[A-Za-z0-9-_]+",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    public String extractGithub(String text) {

        Pattern pattern = Pattern.compile(
                "(https?://)?(www\\.)?github\\.com/[A-Za-z0-9-_]+",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    public String extractPortfolio(String text) {

        Pattern pattern = Pattern.compile(
                "(https?://)?(www\\.)?[A-Za-z0-9.-]+\\.(com|dev|io|me|net)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            String url = matcher.group();

            if (!url.contains("linkedin")
                    && !url.contains("github")) {

                return url;
            }
        }

        return "";
    }

    public List<String> extractSkills(String text) {

        List<String> skills = new ArrayList<>();

        String lower = text.toLowerCase();

        for (String skill : SkillsDatabase.SKILLS) {

            if (lower.contains(skill.toLowerCase())) {

                if (!skills.contains(skill)) {
                    skills.add(skill);
                }
            }
        }

        return skills;
    }

    public int extractExperience(String text) {

        Pattern pattern = Pattern.compile(
                "(\\d+)\\+?\\s*(years|year|yrs|yr)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

    public boolean containsEducation(String text) {

        String lower = text.toLowerCase();

        return lower.contains("education")
                || lower.contains("b.tech")
                || lower.contains("b.e")
                || lower.contains("bachelor")
                || lower.contains("master")
                || lower.contains("mca")
                || lower.contains("bca");
    }

    public boolean containsProjects(String text) {

        String lower = text.toLowerCase();

        return lower.contains("project")
                || lower.contains("projects");
    }

    public boolean containsCertification(String text) {

        String lower = text.toLowerCase();

        return lower.contains("certification")
                || lower.contains("certifications")
                || lower.contains("aws")
                || lower.contains("oracle")
                || lower.contains("azure");
    }
}