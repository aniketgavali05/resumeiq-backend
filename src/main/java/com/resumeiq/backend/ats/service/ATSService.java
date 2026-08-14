package com.resumeiq.backend.ats.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.ats.analyzer.AchievementAnalyzer;
import com.resumeiq.backend.ats.analyzer.ActionVerbAnalyzer;
import com.resumeiq.backend.ats.analyzer.CertificationAnalyzer;
import com.resumeiq.backend.ats.analyzer.EducationAnalyzer;
import com.resumeiq.backend.ats.analyzer.ExperienceAnalyzer;
import com.resumeiq.backend.ats.analyzer.FormattingAnalyzer;
import com.resumeiq.backend.ats.analyzer.GrammarAnalyzer;
import com.resumeiq.backend.ats.analyzer.ProjectAnalyzer;
import com.resumeiq.backend.ats.analyzer.ResumeSectionAnalyzer;
import com.resumeiq.backend.ats.analyzer.SkillAnalyzer;
import com.resumeiq.backend.ats.dto.ATSResponse;
import com.resumeiq.backend.ats.dto.ResumeData;
import com.resumeiq.backend.ats.parser.ResumeParser;

@Service
public class ATSService {

    private final ResumeTextExtractor resumeTextExtractor;
    private final ResumeParser resumeParser;

    private final SkillMatcher skillMatcher;
    private final KeywordAnalyzer keywordAnalyzer;

    private final SkillAnalyzer skillAnalyzer;
    private final ExperienceAnalyzer experienceAnalyzer;
    private final EducationAnalyzer educationAnalyzer;
    private final ProjectAnalyzer projectAnalyzer;
    private final CertificationAnalyzer certificationAnalyzer;
    private final ResumeSectionAnalyzer resumeSectionAnalyzer;
    private final FormattingAnalyzer formattingAnalyzer;
    private final AchievementAnalyzer achievementAnalyzer;
    private final ActionVerbAnalyzer actionVerbAnalyzer;

    private final ATSScorer atsScorer;
    private final GrammarAnalyzer grammarAnalyzer;

    public ATSService(

            ResumeTextExtractor resumeTextExtractor,
            ResumeParser resumeParser,

            SkillMatcher skillMatcher,
            KeywordAnalyzer keywordAnalyzer,

            SkillAnalyzer skillAnalyzer,
            ExperienceAnalyzer experienceAnalyzer,
            EducationAnalyzer educationAnalyzer,
            ProjectAnalyzer projectAnalyzer,
            CertificationAnalyzer certificationAnalyzer,
            ResumeSectionAnalyzer resumeSectionAnalyzer,
            FormattingAnalyzer formattingAnalyzer,
            AchievementAnalyzer achievementAnalyzer,
            ActionVerbAnalyzer actionVerbAnalyzer,

            ATSScorer atsScorer,
            GrammarAnalyzer grammarAnalyzer

    ) {

        this.resumeTextExtractor = resumeTextExtractor;
        this.resumeParser = resumeParser;

        this.skillMatcher = skillMatcher;
        this.keywordAnalyzer = keywordAnalyzer;

        this.skillAnalyzer = skillAnalyzer;
        this.experienceAnalyzer = experienceAnalyzer;
        this.educationAnalyzer = educationAnalyzer;
        this.projectAnalyzer = projectAnalyzer;
        this.certificationAnalyzer = certificationAnalyzer;
        this.resumeSectionAnalyzer = resumeSectionAnalyzer;
        this.formattingAnalyzer = formattingAnalyzer;
        this.achievementAnalyzer = achievementAnalyzer;
        this.actionVerbAnalyzer = actionVerbAnalyzer;

        this.atsScorer = atsScorer;
        this.grammarAnalyzer = grammarAnalyzer;
    }

    public ATSResponse analyze(

            MultipartFile resume,
            String jobDescription

    ) throws Exception {

        String resumeText =
                resumeTextExtractor.extractText(resume);

        ResumeData resumeData =
                resumeParser.parse(resumeText);

        if (!resumeData.isResume()) {

            throw new RuntimeException(
                    "Uploaded file is not a valid resume.");
        }

        List<String> matchedSkills =
                skillMatcher.getMatchedSkills(
                        resumeText,
                        jobDescription);

        List<String> missingSkills =
                skillMatcher.getMissingSkills(
                        resumeText,
                        jobDescription);

        int skillScore =
                skillAnalyzer.calculateSkillScore(
                        resumeText);

        int keywordScore =
                keywordAnalyzer.calculateKeywordScore(
                        resumeText,
                        jobDescription);

        int experienceScore =
                experienceAnalyzer.calculateExperienceScore(
                        resumeText);

        int educationScore =
                educationAnalyzer.calculateEducationScore(
                        resumeText);

        int projectScore =
                projectAnalyzer.calculateProjectScore(
                        resumeText);

        int certificationScore =
                certificationAnalyzer.calculateCertificationScore(
                        resumeText);

        int sectionScore =
                resumeSectionAnalyzer.calculateSectionScore(
                        resumeText);

        int formattingScore =
                formattingAnalyzer.calculateFormattingScore(
                        resumeText);

        int achievementScore =
                achievementAnalyzer.calculateAchievementScore(
                        resumeText);

        int actionVerbScore =
                actionVerbAnalyzer.calculateActionVerbScore(
                        resumeText);

        int grammarScore =
                grammarAnalyzer.calculateGrammarScore(
                        resumeText);

        int readabilityScore =
                grammarAnalyzer.calculateReadabilityScore(
                        resumeText);

        int finalScore =
                atsScorer.calculateFinalScore(

                        skillScore,
                        keywordScore,
                        experienceScore,
                        educationScore,
                        projectScore,
                        certificationScore,
                        sectionScore,
                        formattingScore,
                        achievementScore,
                        actionVerbScore,
                        grammarScore,
                        readabilityScore

                );

        List<String> strengths =
                buildStrengths(

                        skillScore,
                        keywordScore,
                        experienceScore,
                        educationScore,
                        projectScore,
                        certificationScore,
                        sectionScore,
                        formattingScore

                );

        List<String> weaknesses =
                buildWeaknesses(

                        skillScore,
                        keywordScore,
                        experienceScore,
                        educationScore,
                        projectScore,
                        certificationScore,
                        sectionScore,
                        formattingScore

                );

        System.out.println("\n============= ATS REPORT =============");

        System.out.println("Name : " + resumeData.getName());
        System.out.println("Email : " + resumeData.getEmail());

        System.out.println("Skill Score : " + skillScore);
        System.out.println("Keyword Score : " + keywordScore);
        System.out.println("Experience Score : " + experienceScore);
        System.out.println("Education Score : " + educationScore);
        System.out.println("Project Score : " + projectScore);
        System.out.println("Certification Score : " + certificationScore);
        System.out.println("Section Score : " + sectionScore);
        System.out.println("Formatting Score : " + formattingScore);
        System.out.println("Achievement Score : " + achievementScore);
        System.out.println("Action Verb Score : " + actionVerbScore);
        System.out.println("Grammar Score : " + grammarScore);
        System.out.println("Readability Score : " + readabilityScore);

        System.out.println("-------------------------------------");

        System.out.println("Final ATS Score : " + finalScore);
        System.out.println("ATS Rating : " + atsScorer.getScoreLevel(finalScore));
        System.out.println("Grammar Level : "
                + grammarAnalyzer.getGrammarLevel(grammarScore));

        System.out.println("Strengths : " + strengths);
        System.out.println("Weaknesses : " + weaknesses);

        System.out.println("=====================================");

        List<String> suggestions =
                buildSuggestions(

                        finalScore,
                        missingSkills,
                        keywordScore,
                        resumeData,
                        sectionScore,
                        formattingScore,
                        achievementScore,
                        actionVerbScore,
                        grammarScore,
                        readabilityScore

                );

        return new ATSResponse(

                finalScore,

                skillScore,
                keywordScore,
                experienceScore,
                educationScore,
                projectScore,
                certificationScore,
                sectionScore,
                formattingScore,

                atsScorer.getScoreLevel(finalScore),

                matchedSkills,
                missingSkills,

                strengths,
                weaknesses,

                suggestions
        );

    }

    private List<String> buildStrengths(

            int skillScore,
            int keywordScore,
            int experienceScore,
            int educationScore,
            int projectScore,
            int certificationScore,
            int sectionScore,
            int formattingScore

    ) {

        List<String> strengths = new ArrayList<>();

        if (skillScore >= 80)
            strengths.add("Strong technical skills detected.");

        if (keywordScore >= 80)
            strengths.add("Excellent keyword optimization.");

        if (experienceScore >= 80)
            strengths.add("Good work experience.");

        if (educationScore >= 80)
            strengths.add("Education section is complete.");

        if (projectScore >= 80)
            strengths.add("Projects are well presented.");

        if (certificationScore >= 80)
            strengths.add("Professional certifications included.");

        if (sectionScore >= 80)
            strengths.add("Resume contains all important sections.");

        if (formattingScore >= 80)
            strengths.add("ATS-friendly formatting.");

        return strengths;
    }

    private List<String> buildWeaknesses(

            int skillScore,
            int keywordScore,
            int experienceScore,
            int educationScore,
            int projectScore,
            int certificationScore,
            int sectionScore,
            int formattingScore

    ) {

        List<String> weaknesses = new ArrayList<>();

        if (skillScore < 70)
            weaknesses.add("Technical skills can be improved.");

        if (keywordScore < 70)
            weaknesses.add("Missing important ATS keywords.");

        if (experienceScore < 70)
            weaknesses.add("Add more professional experience.");

        if (educationScore < 70)
            weaknesses.add("Education details are incomplete.");

        if (projectScore < 70)
            weaknesses.add("Projects need more details.");

        if (certificationScore < 70)
            weaknesses.add("Relevant certifications are missing.");

        if (sectionScore < 70)
            weaknesses.add("Some resume sections are missing.");

        if (formattingScore < 70)
            weaknesses.add("Resume formatting is not ATS friendly.");

        return weaknesses;
    }

    private List<String> buildSuggestions(

            int score,
            List<String> missingSkills,
            int keywordScore,
            ResumeData resumeData,
            int sectionScore,
            int formattingScore,
            int achievementScore,
            int actionVerbScore,
            int grammarScore,
            int readabilityScore

    ) {

        List<String> suggestions = new ArrayList<>();

        if (!missingSkills.isEmpty()) {
            suggestions.add("Add these missing skills: " + String.join(", ", missingSkills));
        }

        if (keywordScore < 70) {
            suggestions.add("Include more keywords from the Job Description.");
        }

        if (resumeData.getProjects().isEmpty()) {
            suggestions.add("Add 2-3 real-world projects with measurable achievements.");
        }

        if (resumeData.getEducation().isEmpty()) {
            suggestions.add("Add your education details.");
        }

        if (resumeData.getCertifications().isEmpty()) {
            suggestions.add("Include professional certifications.");
        }

        if (resumeData.getExperienceYears() == 0) {
            suggestions.add("Mention internships or work experience.");
        }

        if (sectionScore < 80) {
            suggestions.add("Include all standard resume sections such as Summary, Skills, Experience, Projects and Education.");
        }

        if (formattingScore < 80) {
            suggestions.add("Improve formatting using proper headings, bullet points and consistent spacing.");
        }

        if (achievementScore < 70) {
            suggestions.add("Add measurable achievements using numbers, percentages and business impact.");
        }

        if (actionVerbScore < 70) {
            suggestions.add("Use stronger action verbs like Developed, Designed, Implemented, Led and Optimized.");
        }

        if (grammarScore < 80) {
            suggestions.add("Improve grammar and remove weak or repetitive wording.");
        }

        if (readabilityScore < 80) {
            suggestions.add("Use shorter, clearer sentences to improve readability.");
        }

        if (score < 60) {
            suggestions.add("Resume needs major improvements before applying.");
        } else if (score < 80) {
            suggestions.add("Resume is good but can be improved further.");
        } else {
            suggestions.add("Excellent ATS compatibility. Your resume is ready for most ATS systems.");
        }

        return suggestions;
    }

}