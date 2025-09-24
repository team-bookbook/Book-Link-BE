package com.bookbook.booklink.notification_service.repository;

import com.bookbook.booklink.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findAllByUserId(String userId);

    List<Notification> findAllByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);

}
    