package com.resumeiq.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.backend.response.NotificationResponse;
import com.resumeiq.backend.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService =
                notificationService;
    }

    @GetMapping
    public ResponseEntity<
            List<NotificationResponse>>
    getMyNotifications() {

        return ResponseEntity.ok(
                notificationService
                        .getMyNotifications()
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>>
    getUnreadCount() {

        return ResponseEntity.ok(
                Map.of(
                        "count",
                        notificationService
                                .countUnread()
                )
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse>
    markAsRead(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                notificationService
                        .markAsRead(id)
        );
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void>
    markAllAsRead() {

        notificationService
                .markAllAsRead();

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteNotification(
            @PathVariable Long id
    ) {

        notificationService
                .deleteNotification(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}