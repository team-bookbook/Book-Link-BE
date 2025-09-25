package com.bookbook.booklink.book_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NationalLibraryResponseDto {
    @JsonProperty("TITLE")
    private String title;
    @JsonProperty("AUTHOR")
    private String author;

    @JsonProperty("EA_ISBN")
    private String isbn;

    @JsonProperty("PUBLISHER")
    private String publisher;

    @JsonProperty("PRE_PRICE")
    private String original_price;

    @JsonProperty("REAL_PUBLISH_DATE")
    private String publishDate;
}
