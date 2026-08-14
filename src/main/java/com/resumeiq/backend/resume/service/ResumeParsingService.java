package com.resumeiq.backend.resume.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeParsingService {

    String extractResumeText(MultipartFile file) throws IOException;

}