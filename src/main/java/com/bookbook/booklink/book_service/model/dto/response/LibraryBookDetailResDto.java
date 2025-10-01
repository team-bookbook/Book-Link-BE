package com.bookbook.booklink.book_service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "도서 상세 조회를 위한 도서 상세 응답 DTO")
public class LibraryBookDetailResDto {
    private LibraryDto libraryDto;
    private LibraryBookDetailDto libraryBookDetailDto;
    private BookDetailDto bookDetailDto;
}
