package com.resumeiq.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeiq.backend.response.SkillAnalysisResponse;
import com.resumeiq.backend.service.ResumeSkillAnalysisService;

@RestController
@RequestMapping("/api/skills")
public class ResumeSkillAnalysisController {

    private final ResumeSkillAnalysisService skillAnalysisService;

    public ResumeSkillAnalysisController(
            ResumeSkillAnalysisService skillAnalysisService
    ) {
        this.skillAnalysisService =
                skillAnalysisService;
    }

    @GetMapping
    public ResponseEntity<SkillAnalysisResponse>
    getMySkills() {

        return ResponseEntity.ok(
                skillAnalysisService
                        .analyzeCurrentUser()
        );
    }
}