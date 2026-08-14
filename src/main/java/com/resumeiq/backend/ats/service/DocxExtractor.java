package com.resumeiq.backend.ats.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocxExtractor {

    public String extract(MultipartFile file) {

        try (InputStream input = file.getInputStream()) {

            return extractFromStream(input);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read DOCX resume.",
                    e);
        }
    }

    /**
     * Extract text from a DOCX already stored on disk.
     */
    public String extract(Path path) {

        if (path == null || !Files.exists(path)) {
            throw new RuntimeException(
                    "Stored DOCX resume was not found.");
        }

        try (InputStream input = Files.newInputStream(path)) {

            return extractFromStream(input);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read stored DOCX resume.",
                    e);
        }
    }

    private String extractFromStream(
            InputStream input) throws IOException {

        try (XWPFDocument document =
                     new XWPFDocument(input)) {

            StringBuilder text = new StringBuilder();

            document.getParagraphs().forEach(paragraph -> {

                String line = paragraph.getText();

                if (line != null && !line.isBlank()) {
                    text.append(line).append("\n");
                }
            });

            String result = normalize(text.toString());

            if (result.isBlank()) {

                throw new RuntimeException(
                        "Unable to extract text from DOCX.");
            }

            return result;
        }
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