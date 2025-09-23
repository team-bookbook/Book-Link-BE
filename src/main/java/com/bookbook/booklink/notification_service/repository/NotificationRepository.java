package com.bookbook.booklink.notification_service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.bookbook.booklink.notification_service.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

}
    