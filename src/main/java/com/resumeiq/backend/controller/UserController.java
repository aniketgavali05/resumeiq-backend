package com.resumeiq.backend.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.backend.request.ChangePasswordRequest;
import com.resumeiq.backend.request.UpdateProfileRequest;
import com.resumeiq.backend.request.UpdateSettingsRequest;
import com.resumeiq.backend.response.ApiResponse;
import com.resumeiq.backend.response.UserResponse;
import com.resumeiq.backend.response.UserSettingsResponse;
import com.resumeiq.backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            Authentication authentication
    ) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        UserResponse user =
                userService.getCurrentUser(
                        authentication.getName()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User profile fetched successfully",
                        user
                )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        UserResponse user =
                userService.updateCurrentUser(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User profile updated successfully",
                        user
                )
        );
    }

    @PostMapping(
            value = "/me/profile-image",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ApiResponse<UserResponse>> uploadProfileImage(
            Authentication authentication,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        UserResponse user =
                userService.updateProfileImage(
                        authentication.getName(),
                        file
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Profile picture updated successfully",
                        user
                )
        );
    }

    @DeleteMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<Void>> removeProfileImage(
            Authentication authentication
    ) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        userService.removeProfileImage(
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Profile picture removed successfully",
                        null
                )
        );
    }

    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<UserSettingsResponse>> getSettings(
            Authentication authentication
    ) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        UserSettingsResponse settings =
                userService.getSettings(
                        authentication.getName()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Settings fetched successfully",
                        settings
                )
        );
    }

    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<UserSettingsResponse>> updateSettings(
            Authentication authentication,
            @Valid @RequestBody UpdateSettingsRequest request
    ) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        UserSettingsResponse settings =
                userService.updateSettings(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Settings updated successfully",
                        settings
                )
        );
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Authentication is missing.",
                                    null
                            )
                    );
        }

        userService.changePassword(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Password updated successfully",
                        null
                )
        );
    }
}