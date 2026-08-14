package com.resumeiq.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeiq.backend.response.RoadmapResponse;
import com.resumeiq.backend.service.ResumeRoadmapService;

@RestController
@RequestMapping("/api/roadmap")
public class ResumeRoadmapController {

    private final ResumeRoadmapService roadmapService;

    public ResumeRoadmapController(
            ResumeRoadmapService roadmapService
    ) {
        this.roadmapService = roadmapService;
    }

    @GetMapping
    public ResponseEntity<RoadmapResponse>
    getMyRoadmap() {

        return ResponseEntity.ok(
                roadmapService.getCurrentUserRoadmap()
        );
    }
}