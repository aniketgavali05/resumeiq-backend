package com.resumeiq.backend.resume.parser;

import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocxResumeParser {

    public String extractText(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("DOCX file is empty.");
        }

        try (
                XWPFDocument document =
                        new XWPFDocument(file.getInputStream());

                XWPFWordExtractor extractor =
                        new XWPFWordExtractor(document)
        ) {

            return extractor.getText();
        }
    }
}