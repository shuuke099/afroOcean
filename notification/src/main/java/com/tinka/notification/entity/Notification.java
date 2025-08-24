package com.tinka.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_recipient_id", columnList = "recipientId"),
                @Index(name = "idx_notifications_user_id", columnList = "userId"),
                @Index(name = "idx_notifications_channel", columnList = "channel"),
                @Index(name = "idx_notifications_type", columnList = "notificationType")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // from event
    @Column(length = 100)
    private String recipientId;

    @Column(length = 100)
    private String userId;

    @Column(length = 32)
    private String channel;           // EMAIL/SMS/PUSH

    @Column(length = 32)
    private String recipientType;     // USER/SELLER/ADMIN

    @Column(length = 32)
    private String notificationType;  // EMAIL/SMS/IN_APP

    @Column(length = 100)
    private String templateId;

    // contact/content (quick path via event.data)
    @Column(length = 320)
    private String email;

    @Column(length = 255)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String dataJson;          // serialized event.data for audit

    private boolean sent;

    @Column(length = 1000)
    private String error;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
