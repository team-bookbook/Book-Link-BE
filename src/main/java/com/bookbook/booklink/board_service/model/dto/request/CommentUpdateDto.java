package com.bookbook.booklink.board_service.model.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentUpdateDto {
    private UUID id;

    private String content;
}
