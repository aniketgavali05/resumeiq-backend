
package com.resumeiq.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "user_settings",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_settings_user",
            columnNames = "user_id"
        )
    }
)
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    @Column(nullable = false, length = 30)
    private String language = "English";

    @Column(nullable = false)
    private Boolean emailNotifications = true;

    @Column(nullable = false)
    private Boolean pushNotifications = false;

    @Column(nullable = false)
    private Boolean weeklyDigest = true;

    @Column(nullable = false, length = 20)
    private String theme = "light";

    public UserSettings() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

