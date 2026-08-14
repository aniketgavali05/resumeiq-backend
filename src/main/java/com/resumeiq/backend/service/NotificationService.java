package com.resumeiq.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumeiq.backend.entity.Notification;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.repository.NotificationRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.response.NotificationResponse;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository =
                notificationRepository;
        this.userRepository =
                userRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse>
    getMyNotifications() {

        User currentUser =
                getCurrentUser();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        currentUser.getId()
                )
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countUnread() {

        User currentUser =
                getCurrentUser();

        return notificationRepository
                .countByUserIdAndReadFalse(
                        currentUser.getId()
                );
    }

    @Transactional
    public NotificationResponse
    markAsRead(Long id) {

        User currentUser =
                getCurrentUser();

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found."
                                )
                        );

        verifyOwnership(
                notification,
                currentUser
        );

        notification.setRead(true);

        return toResponse(
                notificationRepository.save(
                        notification
                )
        );
    }

    @Transactional
    public void markAllAsRead() {

        User currentUser =
                getCurrentUser();

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndReadFalseOrderByCreatedAtDesc(
                                currentUser.getId()
                        );

        for (Notification notification :
                notifications) {

            notification.setRead(true);
        }

        notificationRepository.saveAll(
                notifications
        );
    }

    @Transactional
    public void deleteNotification(
            Long id
    ) {

        User currentUser =
                getCurrentUser();

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found."
                                )
                        );

        verifyOwnership(
                notification,
                currentUser
        );

        notificationRepository.delete(
                notification
        );
    }

    /**
     * Internal helper for creating notifications
     * from other backend services later.
     */
    @Transactional
    public NotificationResponse createNotification(
            User user,
            String title,
            String description,
            String type
    ) {

        Notification notification =
                new Notification();

        notification.setUser(user);
        notification.setTitle(title);
        notification.setDescription(
                description
        );

        notification.setType(
                normalizeType(type)
        );

        notification.setRead(false);

        Notification saved =
                notificationRepository.save(
                        notification
                );

        return toResponse(saved);
    }

    private String normalizeType(
            String type
    ) {

        if (type == null
                || type.isBlank()) {
            return "info";
        }

        String normalized =
                type.trim().toLowerCase();

        if (!List.of(
                "info",
                "success",
                "warning",
                "error"
        ).contains(normalized)) {

            return "info";
        }

        return normalized;
    }

    private void verifyOwnership(
            Notification notification,
            User currentUser
    ) {

        if (notification.getUser() == null
                || notification.getUser().getId() == null
                || !notification.getUser()
                        .getId()
                        .equals(currentUser.getId())) {

            throw new ResourceNotFoundException(
                    "Notification not found."
            );
        }
    }

    private NotificationResponse
    toResponse(
            Notification notification
    ) {

        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getDescription(),
                notification.getType(),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new ResourceNotFoundException(
                    "Authenticated user not found."
            );
        }

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        )
                );
    }
}