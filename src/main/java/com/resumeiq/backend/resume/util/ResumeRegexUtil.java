package com.resumeiq.backend.resume.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ResumeRegexUtil {

    private ResumeRegexUtil() {
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "(\\+?\\d{1,3}[\\s-]?)?(\\d{10})");

    public static String extractEmail(String text) {

        if (text == null) {
            return "";
        }

        Matcher matcher = EMAIL_PATTERN.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    public static String extractPhone(String text) {

        if (text == null) {
            return "";
        }

        Matcher matcher = PHONE_PATTERN.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    public static String extractName(String text) {

        if (text == null || text.isBlank()) {
            return "";
        }

        String[] lines = text.split("\\R");

        for (String line : lines) {

            line = line.trim();

            if (!line.isBlank()
                    && !line.contains("@")
                    && line.length() < 60) {

                return line;
            }
        }

        return "";
    }
}