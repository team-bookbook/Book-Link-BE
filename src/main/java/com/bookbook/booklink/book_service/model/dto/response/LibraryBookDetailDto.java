package com.bookbook.booklink.book_service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class LibraryBookDetailDto {
    private final UUID id;
    // 대여 가능, 예약 가능, 대여 중, 예약 중
    private final LibraryBookStatus status;
    private final Integer copies;
    private final Integer deposit;
    private final Integer borrowedCount;

    private final LocalDate expectedReturnDate;
    private final UUID borrowId;
    private final String borrowedStatus; // todo : 추후 enum 변경
    private final UUID reservedId;
}
