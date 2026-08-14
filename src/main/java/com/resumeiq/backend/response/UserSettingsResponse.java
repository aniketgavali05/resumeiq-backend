
package com.resumeiq.backend.response;

public class UserSettingsResponse {

    private String language;
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean weeklyDigest;
    private String theme;

    public UserSettingsResponse() {
    }

    public UserSettingsResponse(
            String language,
            boolean emailNotifications,
            boolean pushNotifications,
            boolean weeklyDigest,
            String theme
    ) {
        this.language = language;
        this.emailNotifications = emailNotifications;
        this.pushNotifications = pushNotifications;
        this.weeklyDigest = weeklyDigest;
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public boolean isPushNotifications() {
        return pushNotifications;
    }

    public boolean isWeeklyDigest() {
        return weeklyDigest;
    }

    public String getTheme() {
        return theme;
    }
}

