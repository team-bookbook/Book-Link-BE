package com.bookbook.booklink.borrow_service.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@ToString
public class BorrowRequestDto {
    UUID libraryBookId;
    LocalDate expectedReturnDate;
}
