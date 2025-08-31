package com.tinka.notification.service;

import com.tinka.common.events.notification.NotificationRequestedEvent;
import com.tinka.notification.entity.Notification;
import com.tinka.notification.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void sendNotification(Notification notification) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(notification.getEmail());
            if (notification.getSubject() != null) {
                helper.setSubject(notification.getSubject());
            }
            helper.setText(notification.getMessage(), true); // HTML content

            mailSender.send(message);
            log.info("Notification email sent to {}", notification.getEmail());

            notification.setSent(true);
            notificationRepository.save(notification);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", notification.getEmail(), e.getMessage(), e);
            notification.setSent(false);
            notification.setError(e.getMessage());
            notificationRepository.save(notification);
        }
    }

    /**
     * Consume a NotificationRequestedEvent from Kafka and process it.
     */
    @KafkaListener(
            topics = "${tinka.kafka.topics.notification.requested}",
            groupId = "notification-service"
    )
    public void handleIncomingEvent(NotificationRequestedEvent event) {
        log.info("Received NotificationRequestedEvent: {}", event);

        Notification notification = Notification.builder()
                .recipientId(event.getRecipientId())
                .email(event.getEmail())
                .subject(event.getSubject())
                .message(event.getMessage())
                .channel(event.getChannel())
                .sent(false)
                .build();

        // Save before sending
        notification = notificationRepository.save(notification);

        if ("EMAIL".equalsIgnoreCase(notification.getChannel()) && notification.getEmail() != null) {
            sendNotification(notification);
        } else {
            log.warn("Unsupported channel {} for notification id={}", notification.getChannel(), notification.getId());
        }
    }
}
