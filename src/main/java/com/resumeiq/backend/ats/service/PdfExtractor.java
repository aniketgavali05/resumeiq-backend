package com.resumeiq.backend.ats.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfExtractor {

    public String extract(MultipartFile file) throws IOException {

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {

            return extractFromDocument(document);

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Failed to read PDF resume.",
                    ex);
        }
    }

    /**
     * Extract text from a PDF already stored on disk.
     */
    public String extract(Path path) throws IOException {

        if (path == null || !Files.exists(path)) {
            throw new RuntimeException(
                    "Stored PDF resume was not found.");
        }

        try (PDDocument document = Loader.loadPDF(path.toFile())) {

            return extractFromDocument(document);

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Failed to read stored PDF resume.",
                    ex);
        }
    }

    private String extractFromDocument(
            PDDocument document) throws IOException {

        if (document.getNumberOfPages() == 0) {
            throw new RuntimeException("PDF is empty.");
        }

        PDFTextStripper stripper = new PDFTextStripper();

        stripper.setSortByPosition(true);

        String text = stripper.getText(document);

        if (text == null || text.isBlank()) {
            throw new RuntimeException(
                    "Unable to extract text from PDF. " +
                    "The PDF may be scanned or image-based.");
        }

        return normalize(text);
    }

    private String normalize(String text) {

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