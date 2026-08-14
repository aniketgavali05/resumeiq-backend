package com.resumeiq.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.backend.response.SavedJobResponse;
import com.resumeiq.backend.service.SavedJobService;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(
            SavedJobService savedJobService
    ) {
        this.savedJobService =
                savedJobService;
    }

    @GetMapping
    public ResponseEntity<List<SavedJobResponse>>
    getMySavedJobs() {

        return ResponseEntity.ok(
                savedJobService.getMySavedJobs()
        );
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<SavedJobResponse>
    saveJob(
            @PathVariable Long jobId
    ) {

        return ResponseEntity.ok(
                savedJobService.saveJob(jobId)
        );
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void>
    unsaveJob(
            @PathVariable Long jobId
    ) {

        savedJobService.unsaveJob(jobId);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{jobId}/status")
    public ResponseEntity<Map<String, Boolean>>
    getSaveStatus(
            @PathVariable Long jobId
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "saved",
                        savedJobService.isJobSaved(
                                jobId
                        )
                )
        );
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>>
    getCount() {

        return ResponseEntity.ok(
                Map.of(
                        "count",
                        savedJobService.countMySavedJobs()
                )
        );
    }
}