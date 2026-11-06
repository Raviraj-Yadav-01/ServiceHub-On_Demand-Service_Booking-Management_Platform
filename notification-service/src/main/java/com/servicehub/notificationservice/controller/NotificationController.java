package com.servicehub.notificationservice.controller;

import com.servicehub.notificationservice.entity.Notification;
import com.servicehub.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get User Notifications
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long recipientId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(recipientId));
    }

    // Get Unread Count
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestParam Long recipientId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(recipientId));
    }

    // Mark single as read
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    // Mark all as read
    @PatchMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(@RequestParam Long recipientId) {
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.ok("All notifications marked as read");
    }

    // Delete Notification
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}
