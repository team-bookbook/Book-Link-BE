package com.bookbook.booklink.board_service.model.dto.request;

import com.bookbook.booklink.board_service.model.BoardCategory;
import lombok.Getter;

@Getter
public class BoardCreateDto {

    // 제목
    private String title;

    // 내용
    private String content;

    // 게시글 카테고리
    private BoardCategory category;
}
