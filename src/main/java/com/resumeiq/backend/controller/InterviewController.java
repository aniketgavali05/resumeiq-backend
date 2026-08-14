package com.resumeiq.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.backend.response.InterviewResponse;
import com.resumeiq.backend.service.InterviewService;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "*")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(
            InterviewService interviewService
    ) {
        this.interviewService =
                interviewService;
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponse>>
    getMyInterviews() {

        return ResponseEntity.ok(
                interviewService.getMyInterviews()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse>
    getInterview(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                interviewService.getInterviewById(id)
        );
    }

    @PostMapping
    public ResponseEntity<InterviewResponse>
    createInterview(
            @RequestParam Long applicationId,
            @RequestParam String interviewType,
            @RequestParam String scheduledAt,
            @RequestParam(required = false)
                    String interviewerName,
            @RequestParam(required = false)
                    String meetingLink,
            @RequestParam(required = false)
                    String location,
            @RequestParam(required = false)
                    String notes
    ) {

        LocalDateTime dateTime =
                LocalDateTime.parse(
                        scheduledAt
                );

        return ResponseEntity.ok(
                interviewService.createInterview(
                        applicationId,
                        interviewType,
                        dateTime,
                        interviewerName,
                        meetingLink,
                        location,
                        notes
                )
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InterviewResponse>
    updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {

        return ResponseEntity.ok(
                interviewService.updateStatus(
                        id,
                        request.get("status")
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteInterview(
            @PathVariable Long id
    ) {

        interviewService.deleteInterview(id);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>>
    getCount() {

        return ResponseEntity.ok(
                Map.of(
                        "count",
                        interviewService
                                .countMyInterviews()
                )
        );
    }
}