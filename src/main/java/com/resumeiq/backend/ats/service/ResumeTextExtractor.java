package com.resumeiq.backend.ats.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeTextExtractor {

    private final PdfExtractor pdfExtractor;
    private final DocxExtractor docxExtractor;

    public ResumeTextExtractor(
            PdfExtractor pdfExtractor,
            DocxExtractor docxExtractor) {

        this.pdfExtractor = pdfExtractor;
        this.docxExtractor = docxExtractor;
    }

    /**
     * Extract text from a newly uploaded resume.
     */
    public String extractText(
            MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Resume file is empty.");
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new RuntimeException(
                    "Invalid file.");
        }

        String extractedText;

        String lower =
                fileName.toLowerCase();

        if (lower.endsWith(".pdf")) {

            extractedText =
                    pdfExtractor.extract(file);

        } else if (lower.endsWith(".docx")) {

            extractedText =
                    docxExtractor.extract(file);

        } else {

            throw new RuntimeException(
                    "Only PDF and DOCX resumes are supported.");
        }

        return cleanText(extractedText);
    }

    /**
     * Extract text from a resume that is already stored on disk.
     */
    public String extractText(
            Path path,
            String fileName) throws IOException {

        if (path == null) {
            throw new RuntimeException(
                    "Resume path is missing.");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new RuntimeException(
                    "Resume file name is missing.");
        }

        String extractedText;

        String lower =
                fileName.toLowerCase();

        if (lower.endsWith(".pdf")) {

            extractedText =
                    pdfExtractor.extract(path);

        } else if (lower.endsWith(".docx")) {

            extractedText =
                    docxExtractor.extract(path);

        } else {

            throw new RuntimeException(
                    "Only PDF and DOCX resumes are supported.");
        }

        return cleanText(extractedText);
    }

    /**
     * Normalize extracted resume text.
     */
    private String cleanText(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\r", "")
                .replace("\t", " ")
                .replaceAll("[ ]{2,}", " ")
                .replaceAll("\n{3,}", "\n\n")
                .replace("•", "- ")
                .replace("▪", "- ")
                .replace("◦", "- ")
                .trim();
    }
}