package com.resumeiq.backend.ats.controller;

import com.resumeiq.backend.ats.dto.ATSResponse;
import com.resumeiq.backend.ats.service.ATSService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ats")
@CrossOrigin(origins = "*")
public class ATSController {

    private final ATSService atsService;

    public ATSController(ATSService atsService) {
        this.atsService = atsService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeResume(

            @RequestParam("resume") MultipartFile resume,

            @RequestParam("jobDescription") String jobDescription

    ) {

        try {

            ATSResponse response =
                    atsService.analyze(resume, jobDescription);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        }

    }

}