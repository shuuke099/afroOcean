package com.tinka.notification.service;

import com.tinka.common.events.notification.NotificationRequestedEvent;
import com.tinka.notification.entity.Notification;

public interface NotificationService {

    void saveNotification(Notification notification);

    void sendNotification(Notification notification);

    void handleIncomingEvent(NotificationRequestedEvent event); // For handling raw Kafka message if needed

}
