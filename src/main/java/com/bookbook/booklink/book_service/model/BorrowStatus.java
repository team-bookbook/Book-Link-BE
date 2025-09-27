package com.bookbook.booklink.book_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BorrowStatus {
    BORROWED("대출 중"),
    EXTENDED("연장 중"),
    OVERDUE("연체 중"),
    RETURNED("반납 완료")
    ;

    private final String description;
}
