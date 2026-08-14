package com.resumeiq.backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    private DateUtils() {
    }

    public static String format(LocalDateTime dateTime) {

        if (dateTime == null) {
            return "";
        }

        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}