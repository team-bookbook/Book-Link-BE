package com.bookbook.booklink.board_service.service;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.Board;
import com.bookbook.booklink.board_service.model.Comment;
import com.bookbook.booklink.board_service.model.dto.request.CommentCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.CommentUpdateDto;
import com.bookbook.booklink.board_service.repository.CommentRepository;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardService boardService;
    private final CommentRepository commentRepository;

    public UUID createComment(CommentCreateDto dto, Member member, String traceId) {
        Board board = boardService.getBoardById(dto.getBoardId());

        Comment parentComment = null;
        if (dto.getParentId() != null) {
            parentComment = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
        }

        Comment newComment = Comment.toEntity(dto, member.getId(), parentComment);

        Comment savedComment = commentRepository.save(newComment);

        if (parentComment != null) {
            parentComment.comment();
        }

        board.comment();

        return savedComment.getId();
    }

    public UUID updateComment(CommentUpdateDto commentUpdateDto, Member member, String traceId) {

        Comment exsitingComment = getCommentById(commentUpdateDto.getId());

        exsitingComment.update(commentUpdateDto);

        Comment savedComment = commentRepository.save(exsitingComment);

        return savedComment.getId();
    }

    public Comment getCommentById(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
