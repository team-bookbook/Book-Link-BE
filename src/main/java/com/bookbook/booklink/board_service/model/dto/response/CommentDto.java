package com.bookbook.booklink.board_service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CommentDto {
    private UUID id;
    private String content;
    private UUID memberId;
    private LocalDateTime createdAt;
    private Boolean isUpdated;
    private Integer likeCount;
    private List<CommentDto> children;
}
