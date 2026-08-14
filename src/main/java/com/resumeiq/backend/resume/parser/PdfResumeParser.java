package com.resumeiq.backend.resume.parser;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfResumeParser {

    public String extractText(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF file is empty.");
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {

            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setSortByPosition(true);

            return stripper.getText(document);
        }
    }
}