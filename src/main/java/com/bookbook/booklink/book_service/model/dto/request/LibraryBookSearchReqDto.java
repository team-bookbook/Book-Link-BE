package com.bookbook.booklink.book_service.model.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class LibraryBookSearchReqDto {
    // todo : swagger 설명 추가

    Double latitude;

    Double longitude;

    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
    private int page = 0; // 기본값 0

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private int size = 10; // 기본값 10

    private String bookName; // 검색어 (nullable)

    private LibraryBookSortType sortType = LibraryBookSortType.DISTANCE; // 정렬 조건, 기본값 거리순

}