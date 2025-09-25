package com.bookbook.booklink.book_service.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDto {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String original_price;
    private String publishDate;
}
