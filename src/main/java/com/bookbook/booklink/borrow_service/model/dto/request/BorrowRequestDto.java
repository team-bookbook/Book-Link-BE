package com.bookbook.booklink.borrow_service.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@ToString
public class BorrowRequestDto {

    @NotNull(message = "도서관별 도서 id는 필수입니다.")
    UUID libraryBookId;
    @NotNull(message = "반납 일자는 필수입니다.")
    LocalDate expectedReturnDate;
}
