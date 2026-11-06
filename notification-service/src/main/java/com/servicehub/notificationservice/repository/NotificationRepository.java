package com.servicehub.notificationservice.repository;

import com.servicehub.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // User ke saare notifications latest pehle laane ke liye
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    // Unread (jo padhe nahi gaye) notifications count karne ke liye
    long countByRecipientIdAndIsReadFalse(Long recipientId);
}