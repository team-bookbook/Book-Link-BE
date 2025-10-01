package com.bookbook.booklink.board_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 카테고리 유형")
public enum BoardCategory {
    @Schema(description = "일반 게시판")
    GENERAL,
    @Schema(description = "인기 게시판")
    POPULAR,
    @Schema(description = "추천 게시판")
    RECOMMEND
}