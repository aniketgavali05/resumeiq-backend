package com.resumeiq.backend.resume.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.resume.parser.DocxResumeParser;
import com.resumeiq.backend.resume.parser.PdfResumeParser;

@Service
public class ResumeParsingServiceImpl implements ResumeParsingService {

    private final PdfResumeParser pdfResumeParser;
    private final DocxResumeParser docxResumeParser;

    public ResumeParsingServiceImpl(
            PdfResumeParser pdfResumeParser,
            DocxResumeParser docxResumeParser) {

        this.pdfResumeParser = pdfResumeParser;
        this.docxResumeParser = docxResumeParser;
    }

    @Override
    public String extractResumeText(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file is empty.");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file.");
        }

        fileName = fileName.toLowerCase();

        if (fileName.endsWith(".pdf")) {
            return pdfResumeParser.extractText(file);
        }

        if (fileName.endsWith(".docx")) {
            return docxResumeParser.extractText(file);
        }

        throw new IllegalArgumentException(
                "Only PDF and DOCX files are supported.");
    }
}