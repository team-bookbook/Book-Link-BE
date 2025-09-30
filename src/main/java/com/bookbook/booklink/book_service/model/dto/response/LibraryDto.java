package com.bookbook.booklink.book_service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LibraryDto {
    private final UUID id;
    private final String name;
    private final Double latitude;
    private final Double longitude;
}
