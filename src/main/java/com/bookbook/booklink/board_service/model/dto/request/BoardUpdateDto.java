package com.bookbook.booklink.board_service.model.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BoardUpdateDto {

    private UUID boardId;

    // 제목
    private String title;

    // 내용
    private String content;

}
