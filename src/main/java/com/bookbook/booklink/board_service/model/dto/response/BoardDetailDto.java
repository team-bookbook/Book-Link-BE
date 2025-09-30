package com.bookbook.booklink.board_service.model.dto.response;

import com.bookbook.booklink.board_service.model.Board;
import com.bookbook.booklink.board_service.model.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailDto {

    private UUID id;

    private String writerName;

    // 제목
    private String title;

    // 내용
    private String content;

    private LocalDateTime createdAt;

    private Boolean isUpdated;

    // 좋아요수
    private Integer likeCount = 0;

    // 댓글수
    private Integer commentCount = 0;

    // 게시글 카테고리
    private BoardCategory category;

    // 조회수
    private Integer viewCount = 0;

    public static BoardDetailDto fromEntity(Board board) {
        return BoardDetailDto.builder()
                .id(board.getId())
                .writerName(board.getWriterName())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .isUpdated(board.getUpdatedAt() != null)
                .likeCount(board.getLikeCount())
                .commentCount(board.getCommentCount())
                .category(board.getCategory())
                .viewCount(board.getViewCount())
                .build();
    }
}
