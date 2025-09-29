package com.bookbook.booklink.book_service.model.dto.response;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LibraryBookListDto {
    // todo : swagger 설명 추가

    // 대표 이미지 URL
    private final String imageUrl;

    // 책 제목
    private final String title;

    // 작가
    private final String author;

    // 도서관 이름
    private final String libraryName;

    // 책 개수
    private final int totalCount;

    // 현재 대여 중인 사람 수
    private final int borrowedCount;

    // 보증금
    private final int deposit;

    // 대여중인 경우 (책개수 == 대여중인 사람 수) → true
    private final boolean rentedOut;

    // 예상 반납 기한 (모든 책이 대여중일 때만 표시, nullable)
    private final LocalDate expectedReturnDate;
}
