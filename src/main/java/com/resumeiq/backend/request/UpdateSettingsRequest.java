
package com.resumeiq.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateSettingsRequest {

    @NotBlank
    @Size(max = 30)
    private String language;

    @NotNull
    private Boolean emailNotifications;

    @NotNull
    private Boolean pushNotifications;

    @NotNull
    private Boolean weeklyDigest;

    @NotBlank
    @Size(max = 20)
    private String theme;

    public UpdateSettingsRequest() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(
            Boolean emailNotifications
    ) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(
            Boolean pushNotifications
    ) {
        this.pushNotifications = pushNotifications;
    }

    public Boolean getWeeklyDigest() {
        return weeklyDigest;
    }

    public void setWeeklyDigest(
            Boolean weeklyDigest
    ) {
        this.weeklyDigest = weeklyDigest;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}

