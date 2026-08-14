package com.resumeiq.backend.service;

import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.request.ChangePasswordRequest;
import com.resumeiq.backend.request.UpdateProfileRequest;
import com.resumeiq.backend.request.UpdateSettingsRequest;
import com.resumeiq.backend.response.UserResponse;
import com.resumeiq.backend.response.UserSettingsResponse;

public interface UserService {

    // Profile
    UserResponse getCurrentUser(String email);

    UserResponse updateCurrentUser(
            String email,
            UpdateProfileRequest request
    );

    UserResponse updateProfileImage(
            String email,
            MultipartFile file
    );

    void removeProfileImage(String email);

    // Settings
    UserSettingsResponse getSettings(String email);

    UserSettingsResponse updateSettings(
            String email,
            UpdateSettingsRequest request
    );

    // Password
    void changePassword(
            String email,
            ChangePasswordRequest request
    );
}