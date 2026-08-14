package com.resumeiq.backend.response;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private String title;
    private String description;
    private String type;
    private Boolean read;
    private LocalDateTime createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(
            Long id,
            String title,
            String description,
            String type,
            Boolean read,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Boolean getRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}