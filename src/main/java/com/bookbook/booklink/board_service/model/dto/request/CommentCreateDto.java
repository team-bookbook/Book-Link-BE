package com.bookbook.booklink.board_service.model.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentCreateDto {
    private UUID boardId;
    private UUID parentId; // null이면 최상위 댓글
    private String content;
}
