package com.resumeiq.backend.util;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getFileExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public static boolean isPdf(String filename) {
        return "pdf".equalsIgnoreCase(getFileExtension(filename));
    }

    public static boolean isDoc(String filename) {
        String extension = getFileExtension(filename);

        return extension.equalsIgnoreCase("doc")
                || extension.equalsIgnoreCase("docx");
    }

    public static boolean isAllowedResume(String filename) {
        return isPdf(filename) || isDoc(filename);
    }
}