package com.bookbook.booklink.book_service.model.dto.response;

import com.bookbook.booklink.book_service.model.BookCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookResponseDto {
    private UUID id;
    private String title;
    private String author;
    private String publisher;
    private BookCategory category;
    private String ISBN;
    private String originalPrice;
    private String publishedDate;
}
