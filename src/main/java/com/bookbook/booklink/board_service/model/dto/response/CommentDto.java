package com.bookbook.booklink.board_service.model.dto.response;

import com.bookbook.booklink.board_service.model.Comment;
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
    private String writerName;
    private LocalDateTime createdAt;
    private Boolean isUpdated;
    private Integer likeCount;
    private List<CommentDto> children;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writerName(comment.getWriterName())
                .createdAt(comment.getCreatedAt())
                .isUpdated(comment.getUpdatedAt() != null)
                .likeCount(comment.getLikeCount())
                .children(comment.getChildren().stream()
                        .filter(c -> c.getDeletedAt() == null)
                        .map(CommentDto::fromEntity)
                        .toList())
                .build();
    }
}
