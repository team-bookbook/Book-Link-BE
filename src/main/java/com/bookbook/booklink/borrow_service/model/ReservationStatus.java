package com.bookbook.booklink.borrow_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    WAITING("예약 대기"),
    AVAILABLE("대출 가능"),
    CANCELLED("예약 취소"),
    EXPIRED("만료"),
    BORROWED("대출 완료"),
    ;

    private final String description;

    public boolean canTransitionTo(ReservationStatus nextStatus) {
        return switch (this) {
            case WAITING -> nextStatus == AVAILABLE || nextStatus == CANCELLED;
            case AVAILABLE -> nextStatus == CANCELLED || nextStatus == EXPIRED || nextStatus == BORROWED;
            default -> false;
        };
    }
}
