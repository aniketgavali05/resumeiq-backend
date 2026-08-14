package com.resumeiq.backend.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.response.ResumePageResponse;
import com.resumeiq.backend.response.ResumeResponse;
import com.resumeiq.backend.service.ResumeService;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // =====================================================
    // Upload Resume
    // =====================================================

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription)
            throws IOException {

        return ResponseEntity.ok(
                resumeService.uploadResume(file, jobDescription)
        );
    }

    // =====================================================
    // Resume History
    // =====================================================

    @GetMapping
    public ResponseEntity<ResumePageResponse> getResumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "newest") String sort
    ) {

        ResumePageResponse response =
                resumeService.getResumes(
                        page,
                        size,
                        search,
                        sort
                );

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // Get Resume ATS Analysis
    // =====================================================

    @GetMapping("/{id}/analysis")
    public ResponseEntity<ResumeResponse> getResumeAnalysis(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                resumeService.getResumeAnalysis(id)
        );
    }

    // =====================================================
    // Delete Resume
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Long id) {

        resumeService.deleteResume(id);

        return ResponseEntity.noContent().build();
    }
}