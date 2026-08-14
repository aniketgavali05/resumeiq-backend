package com.resumeiq.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.backend.request.CreateApplicationRequest;
import com.resumeiq.backend.response.ApplicationResponse;
import com.resumeiq.backend.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
@Validated
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    // =====================================================
    // Apply for a job
    // =====================================================

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @Valid @RequestBody CreateApplicationRequest request) {

        ApplicationResponse response =
                applicationService.createApplication(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
    // Get current user's applications
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {

        return ResponseEntity.ok(
                applicationService.getMyApplications()
        );
    }

    // =====================================================
    // Get application by ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                applicationService.getApplicationById(id)
        );
    }

    // =====================================================
    // Update application status
    // =====================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");

        return ResponseEntity.ok(
                applicationService.updateStatus(
                        id,
                        status
                )
        );
    }

    // =====================================================
    // Delete application
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id) {

        applicationService.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }
}