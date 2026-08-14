package com.resumeiq.backend.ats.parser;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class JobDescriptionParser {

    public List<String> parseLines(String jobDescription) {

        return Arrays.stream(jobDescription.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();
    }

    public String normalize(String jobDescription) {

        return jobDescription
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
    }

}