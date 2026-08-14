package com.resumeiq.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.backend.request.CreateCoverLetterRequest;
import com.resumeiq.backend.response.CoverLetterResponse;
import com.resumeiq.backend.service.CoverLetterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cover-letters")
@CrossOrigin(origins = "*")
@Validated
public class CoverLetterController {

    private final CoverLetterService coverLetterService;

    public CoverLetterController(
            CoverLetterService coverLetterService
    ) {
        this.coverLetterService =
                coverLetterService;
    }

    @GetMapping
    public ResponseEntity<List<CoverLetterResponse>>
    getMyCoverLetters() {

        return ResponseEntity.ok(
                coverLetterService
                        .getMyCoverLetters()
        );
    }

    @PostMapping
    public ResponseEntity<CoverLetterResponse>
    createCoverLetter(
            @Valid
            @RequestBody
            CreateCoverLetterRequest request
    ) {

        return ResponseEntity.ok(
                coverLetterService
                        .createCoverLetter(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoverLetterResponse>
    getCoverLetter(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                coverLetterService
                        .getCoverLetterById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteCoverLetter(
            @PathVariable Long id
    ) {

        coverLetterService
                .deleteCoverLetter(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}