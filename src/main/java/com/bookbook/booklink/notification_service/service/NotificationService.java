package com.bookbook.booklink.notification_service.service;

import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.notification_service.model.Notification;
import com.bookbook.booklink.notification_service.model.dto.response.NotificationResDto;
import com.bookbook.booklink.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationResDto> getNotifications(String userId) {

        List<Notification> notificationList = notificationRepository.findAllByUserId(userId);

        return notificationList.stream().map(NotificationResDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResDto> getUnreadNotifications(String userId) {

        List<Notification> notificationList = notificationRepository.findAllByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

        return notificationList.stream().map(NotificationResDto::fromEntity).toList();
    }

    @Transactional
    public void readNotification(UUID notificationId) {
        Notification notification = findNotificationById(notificationId);

        notification.read();

        notificationRepository.save(notification);
    }

    public Notification findNotificationById(UUID notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }
}
    