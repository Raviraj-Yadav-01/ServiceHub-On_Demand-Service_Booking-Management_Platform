package com.servicehub.notificationservice.kafka;

import com.servicehub.notificationservice.entity.Notification;
import com.servicehub.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final NotificationRepository notificationRepository;

    // Main Consumer (Payment Events)
    @KafkaListener(topics = "payment-notification-topic", groupId = "notification-group")
    public void consumePaymentEvent(PaymentEventDto event) {

        System.out.println("✅ Processing Event for Booking ID: " + event.getBookingId());

        // Idempotency Check: Agar same txn ka notification already DB mein hai toh skip karein
        String notificationMessage = "Payment of ₹" + event.getAmount() + " (" + event.getStatus() + ") for Booking #" + event.getBookingId();

        Notification notification = Notification.builder()
                .recipientId(event.getCustomerId())
                .type("PAYMENT_" + event.getStatus())
                .message(notificationMessage)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        System.out.println("💾 Saved Notification to DB successfully!");
    }


    // Dead Letter Topic (DLT) Consumer - Failed Events Monitoring
    @KafkaListener(topics = "payment-notification-topic.DLT", groupId = "notification-dlt-group")
    public void consumeDLTEvent(PaymentEventDto event) {
        System.err.println("🚨 ALERT: Event pushed to DLT after max retries! Booking ID: " + event.getBookingId());
        // Yahan failed events log hotey hain taaki support team investigate kar sake
    }
}