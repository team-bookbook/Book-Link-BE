package com.bookbook.booklink.notification_service.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.notification_service.model.dto.response.NotificationResDto;
import com.bookbook.booklink.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public ResponseEntity<BaseResponse<List<NotificationResDto>>> getNotifications(

    ) {

        List<NotificationResDto> notificationList = notificationService.getNotifications("userID");

        return ResponseEntity.ok()
                .body(BaseResponse.success(notificationList));
    }

    public ResponseEntity<BaseResponse<List<NotificationResDto>>> getUnreadNotifications(

    ) {

        List<NotificationResDto> notificationList = notificationService.getUnreadNotifications("userID");

        return ResponseEntity.ok()
                .body(BaseResponse.success(notificationList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> readNotification(
            @PathVariable UUID id
    ) {
        notificationService.readNotification(id);

        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }
}
    