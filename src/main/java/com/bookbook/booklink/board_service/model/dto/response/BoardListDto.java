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
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {

    // 고유식별자
    private UUID id;

    private String writerName;

    // 제목
    private String title;

    private LocalDateTime createdAt;

    // 좋아요수
    private Integer likeCount;

    // 댓글수
    private Integer commentCount;

    // 게시글 카테고리
    private BoardCategory category;

    // 조회수
    private Integer viewCount = 0;

    public static BoardListDto fromEntity(Board board) {
        return BoardListDto.builder()
                .id(board.getId())
                .writerName(board.getWriterName())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .likeCount(board.getLikeCount())
                .commentCount(board.getCommentCount())
                .category(board.getCategory())
                .viewCount(board.getViewCount())
                .build();
    }

}
