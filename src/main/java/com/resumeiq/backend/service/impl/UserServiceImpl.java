package com.resumeiq.backend.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.entity.UserSettings;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.mapper.UserMapper;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.repository.UserSettingsRepository;
import com.resumeiq.backend.request.ChangePasswordRequest;
import com.resumeiq.backend.request.UpdateProfileRequest;
import com.resumeiq.backend.request.UpdateSettingsRequest;
import com.resumeiq.backend.response.UserResponse;
import com.resumeiq.backend.response.UserSettingsResponse;
import com.resumeiq.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private static final long MAX_PROFILE_IMAGE_SIZE =
            5L * 1024L * 1024L;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSettingsRepository userSettingsRepository;
    private final PasswordEncoder passwordEncoder;

    private final Path profileImageDirectory;
    private final String backendBaseUrl;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            UserSettingsRepository userSettingsRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.upload.profile-dir:uploads/profile-images}")
            String profileImageDirectory,
            @Value("${app.backend-base-url:http://localhost:8082}")
            String backendBaseUrl
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userSettingsRepository = userSettingsRepository;
        this.passwordEncoder = passwordEncoder;

        this.profileImageDirectory =
                Paths.get(profileImageDirectory)
                        .toAbsolutePath()
                        .normalize();

        this.backendBaseUrl =
                backendBaseUrl.replaceAll("/+$", "");
    }

    // =====================================================
    // PROFILE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {

        User user = getUser(email);

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUser(
            String email,
            UpdateProfileRequest request
    ) {

        User user = getUser(email);

        String firstName =
                request.getFirstName().trim();

        String lastName =
                request.getLastName().trim();

        user.setFirstName(firstName);
        user.setLastName(lastName);

        User saved =
                userRepository.save(user);

        log.info(
                "User profile updated successfully for {}",
                email
        );

        return userMapper.toResponse(saved);
    }

@Override
@Transactional
public UserResponse updateProfileImage(
        String email,
        MultipartFile file
) {

    if (file == null || file.isEmpty()) {
        throw new IllegalArgumentException(
                "Please select a profile image."
        );
    }

    if (file.getSize() > MAX_PROFILE_IMAGE_SIZE) {
        throw new IllegalArgumentException(
                "Profile image must be 5 MB or smaller."
        );
    }

    String contentType = file.getContentType();

    if (contentType == null
            || !isAllowedImageType(contentType)) {

        throw new IllegalArgumentException(
                "Only JPG, JPEG, PNG, and WebP images are allowed."
        );
    }

    User user = getUser(email);

    try {

        Files.createDirectories(
                profileImageDirectory
        );

        String extension =
                getExtension(contentType);

        String fileName =
                UUID.randomUUID() + extension;

        Path target =
                profileImageDirectory
                        .resolve(fileName)
                        .normalize();

        if (!target.startsWith(
                profileImageDirectory
        )) {
            throw new IllegalArgumentException(
                    "Invalid profile image path."
            );
        }

        deleteExistingProfileImage(
                user.getProfileImage()
        );

        Files.copy(
                file.getInputStream(),
                target,
                StandardCopyOption.REPLACE_EXISTING
        );

        String imageUrl =
                backendBaseUrl
                        + "/uploads/profile-images/"
                        + fileName;

        user.setProfileImage(imageUrl);

        User saved =
                userRepository.save(user);

        log.info(
                "Profile image updated successfully for {}",
                email
        );

        return userMapper.toResponse(saved);

    } catch (IOException ex) {

        log.error(
                "Failed to save profile image for {}",
                email,
                ex
        );

        throw new IllegalStateException(
                "Unable to save profile image. Please try again.",
                ex
        );
    }
}


    @Override
    @Transactional
    public void removeProfileImage(String email) {

        User user = getUser(email);

        deleteExistingProfileImage(
                user.getProfileImage()
        );

        user.setProfileImage(null);

        userRepository.save(user);

        log.info(
                "Profile image removed successfully for {}",
                email
        );
    }

    private boolean isAllowedImageType(
            String contentType
    ) {

        return "image/jpeg".equalsIgnoreCase(contentType)
                || "image/jpg".equalsIgnoreCase(contentType)
                || "image/png".equalsIgnoreCase(contentType)
                || "image/webp".equalsIgnoreCase(contentType);
    }

    private String getExtension(
            String contentType
    ) {

        return switch (contentType.toLowerCase()) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/jpeg", "image/jpg" -> ".jpg";
            default -> throw new IllegalArgumentException(
                    "Unsupported image type."
            );
        };
    }

    private void deleteExistingProfileImage(
            String profileImage
    ) {

        if (profileImage == null
                || profileImage.isBlank()) {
            return;
        }

        try {
            String marker =
                    "/uploads/profile-images/";

            int markerIndex =
                    profileImage.indexOf(marker);

            if (markerIndex < 0) {
                return;
            }

            String fileName =
                    profileImage.substring(
                            markerIndex + marker.length()
                    );

            Path existingFile =
                    profileImageDirectory
                            .resolve(fileName)
                            .normalize();

            if (existingFile.startsWith(
                    profileImageDirectory
            )) {

                Files.deleteIfExists(existingFile);
            }

        } catch (IOException ex) {

            log.warn(
                    "Unable to delete old profile image",
                    ex
            );
        }
    }

    // =====================================================
    // SETTINGS
    // =====================================================

    @Override
    @Transactional
    public UserSettingsResponse getSettings(
            String email
    ) {

        User user = getUser(email);

        UserSettings settings =
                userSettingsRepository
                        .findByUserId(user.getId())
                        .orElseGet(
                                () -> createDefaultSettings(user)
                        );

        return toSettingsResponse(settings);
    }

    @Override
    @Transactional
    public UserSettingsResponse updateSettings(
            String email,
            UpdateSettingsRequest request
    ) {

        User user = getUser(email);

        UserSettings settings =
                userSettingsRepository
                        .findByUserId(user.getId())
                        .orElseGet(
                                () -> createDefaultSettings(user)
                        );

        String language =
                request.getLanguage().trim();

        String theme =
                request.getTheme()
                        .trim()
                        .toLowerCase();

        if (!"light".equals(theme)
                && !"dark".equals(theme)) {

            throw new IllegalArgumentException(
                    "Theme must be light or dark."
            );
        }

        settings.setLanguage(language);

        settings.setEmailNotifications(
                request.getEmailNotifications()
        );

        settings.setPushNotifications(
                request.getPushNotifications()
        );

        settings.setWeeklyDigest(
                request.getWeeklyDigest()
        );

        settings.setTheme(theme);

        UserSettings saved =
                userSettingsRepository.save(settings);

        return toSettingsResponse(saved);
    }

    // =====================================================
    // PASSWORD
    // =====================================================

    @Override
    @Transactional
    public void changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        User user = getUser(email);

        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPassword()
                );

        if (!currentPasswordMatches) {
            throw new IllegalArgumentException(
                    "Current password is incorrect."
            );
        }

        boolean samePassword =
                passwordEncoder.matches(
                        request.getNewPassword(),
                        user.getPassword()
                );

        if (samePassword) {
            throw new IllegalArgumentException(
                    "New password must be different from current password."
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        log.info(
                "Password changed successfully for {}",
                email
        );
    }

    // =====================================================
    // HELPERS
    // =====================================================

    private User getUser(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    private UserSettings createDefaultSettings(
            User user
    ) {

        UserSettings settings =
                new UserSettings();

        settings.setUser(user);
        settings.setLanguage("English");
        settings.setEmailNotifications(true);
        settings.setPushNotifications(false);
        settings.setWeeklyDigest(true);
        settings.setTheme("light");

        return userSettingsRepository.save(settings);
    }

    private UserSettingsResponse toSettingsResponse(
            UserSettings settings
    ) {

        return new UserSettingsResponse(
                settings.getLanguage(),
                Boolean.TRUE.equals(
                        settings.getEmailNotifications()
                ),
                Boolean.TRUE.equals(
                        settings.getPushNotifications()
                ),
                Boolean.TRUE.equals(
                        settings.getWeeklyDigest()
                ),
                settings.getTheme()
        );
    }
}