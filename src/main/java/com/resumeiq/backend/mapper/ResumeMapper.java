package com.resumeiq.backend.mapper;

import org.springframework.stereotype.Component;

import com.resumeiq.backend.entity.Resume;
import com.resumeiq.backend.response.ResumeResponse;

@Component
public class ResumeMapper {

    public ResumeResponse toResponse(Resume resume) {

        if (resume == null) {
            return null;
        }

        ResumeResponse response = new ResumeResponse();

        response.setId(resume.getId());
        response.setOriginalFileName(resume.getOriginalFileName());
        response.setStoredFileName(resume.getStoredFileName());
        response.setFileType(resume.getFileType());
        response.setFileSize(resume.getFileSize());
        response.setResumeScore(resume.getResumeScore());
        response.setUploadedAt(resume.getUploadedAt());

        return response;
    }
}