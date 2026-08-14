package com.resumeiq.backend.constant;

public final class AppConstants {

    private AppConstants() {
    }

    // Pagination
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 50;

    // Resume Upload
    public static final String RESUME_FOLDER = "uploads/resumes/";

    // Profile Upload
    public static final String PROFILE_FOLDER = "uploads/profile/";

    // File Size (5 MB)
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    // ATS
    public static final int DEFAULT_RESUME_SCORE = 0;
}