package com.bookbook.booklink.book_service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryBookDetailResDto {
    private LibraryDto libraryDto;
    private LibraryBookDetailDto libraryBookDetailDto;
    private BookDetailDto bookDetailDto;
}
