package com.resumeiq.backend.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCoverLetterRequest {

    @NotNull
    private Long applicationId;

    @Size(max = 50)
    private String tone;

    public CreateCoverLetterRequest() {
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(
            Long applicationId
    ) {
        this.applicationId = applicationId;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(
            String tone
    ) {
        this.tone = tone;
    }
}