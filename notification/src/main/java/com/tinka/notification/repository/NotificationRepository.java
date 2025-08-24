package com.tinka.notification.repository;

import com.tinka.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(String userId);

    List<Notification> findByEmail(String email);

    List<Notification> findBySentFalse(); // For retry logic if needed

}
