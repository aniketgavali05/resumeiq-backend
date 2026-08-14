package com.resumeiq.backend.jobmatching.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeiq.backend.jobmatching.dto.JobMatchRequest;
import com.resumeiq.backend.jobmatching.dto.JobMatchResponse;
import com.resumeiq.backend.jobmatching.dto.MatchResult;
import com.resumeiq.backend.jobmatching.service.JobMatchingService;

@RestController
@RequestMapping("/api/jobmatching")
@CrossOrigin(origins = "http://localhost:3000")
public class JobMatchingController {

    private final JobMatchingService jobMatchingService;

    public JobMatchingController(
            JobMatchingService jobMatchingService) {

        this.jobMatchingService = jobMatchingService;
    }

    /**
     * Match a supplied resume against a supplied job description.
     *
     * Existing endpoint retained for compatibility.
     */
    @PostMapping("/match")
    public ResponseEntity<JobMatchResponse> matchJob(
            @RequestBody JobMatchRequest request) {

        return ResponseEntity.ok(
                jobMatchingService.matchJob(
                        request.getResumeText(),
                        request.getJobDescription()));
    }

    /**
     * Match the current user's latest resume against
     * all active jobs in PostgreSQL.
     */
    @GetMapping("/matches")
    public ResponseEntity<List<MatchResult>> getMatches() {

        return ResponseEntity.ok(
                jobMatchingService.getMatchesForCurrentUser());
    }
}