package com.servicehub.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId; // User ya Provider ka ID

    private String type; // PAYMENT_SUCCESS, BOOKING_CONFIRMED, etc.

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;
}