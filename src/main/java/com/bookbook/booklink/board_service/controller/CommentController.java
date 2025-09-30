package com.bookbook.booklink.board_service.controller;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.dto.request.CommentCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.CommentUpdateDto;
import com.bookbook.booklink.board_service.model.dto.response.CommentDto;
import com.bookbook.booklink.board_service.service.CommentService;
import com.bookbook.booklink.common.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글/대댓글 생성
    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> createComment(
            @RequestBody CommentCreateDto dto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {
        UUID newCommentId = commentService.createComment(dto, member, traceId);
        return ResponseEntity.ok(BaseResponse.success(newCommentId));
    }

    // 2. 댓글/대댓글 수정
    @PutMapping
    public ResponseEntity<BaseResponse<UUID>> updateComment(
            @RequestHeader("Trace-Id") String traceId,
            @RequestBody CommentUpdateDto dto,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {
        UUID updatedId = commentService.updateComment(dto, member, traceId);
        return ResponseEntity.ok(BaseResponse.success(updatedId));
    }

    // 3. 댓글/대댓글 삭제 (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> deleteComment(
            @PathVariable UUID id
    ) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(BaseResponse.success(Boolean.TRUE));
    }

    // 4. 게시글에 대한 댓글 리스트 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BaseResponse<List<CommentDto>>> getCommentsByBoard(
            @PathVariable UUID boardId
    ) {
        List<CommentDto> comments = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(BaseResponse.success(comments));
    }

    // 6. 댓글 좋아요
    @PatchMapping("/{id}/like")
    public ResponseEntity<BaseResponse<Boolean>> likeComment(
            @PathVariable UUID id
    ) {
        commentService.likeComment(id);
        return ResponseEntity.ok(BaseResponse.success(Boolean.TRUE));
    }
}
