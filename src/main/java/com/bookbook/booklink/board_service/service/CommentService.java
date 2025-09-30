package com.bookbook.booklink.board_service.service;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.Comment;
import com.bookbook.booklink.board_service.model.dto.request.CommentCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.CommentUpdateDto;
import com.bookbook.booklink.board_service.model.dto.response.CommentDto;
import com.bookbook.booklink.board_service.repository.CommentRepository;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardService boardService;
    private final CommentRepository commentRepository;

    @Transactional
    public UUID createComment(CommentCreateDto dto, Member member, String traceId) {

        Comment parentComment = null;
        if (dto.getParentId() != null) {
            parentComment = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
            if (parentComment.getParent() != null) {
                throw new CustomException(ErrorCode.TOO_MANY_PARENT);
            }
        }

        Comment newComment = Comment.toEntity(dto, member, parentComment);

        Comment savedComment = commentRepository.save(newComment);

        if (parentComment != null) {
            parentComment.getChildren().add(savedComment);
            parentComment.comment();
            commentRepository.save(parentComment);
        }

        boardService.commentBoard(dto.getBoardId());

        return savedComment.getId();
    }

    @Transactional
    public UUID updateComment(CommentUpdateDto commentUpdateDto, Member member, String traceId) {

        Comment exsitingComment = getCommentById(commentUpdateDto.getId());

        exsitingComment.update(commentUpdateDto);

        Comment savedComment = commentRepository.save(exsitingComment);

        return savedComment.getId();
    }

    @Transactional
    public void deleteComment(UUID commentId) {
        Comment comment = getCommentById(commentId);

        comment.delete();

        commentRepository.save(comment);
    }

    @Transactional
    public void likeComment(UUID commentId) {
        Comment comment = getCommentById(commentId);
        comment.like();
        commentRepository.save(comment);
    }

    public List<CommentDto> getCommentsByBoard(UUID boardId) {
        List<Comment> topLevelComments = commentRepository
                .findByBoardIdAndParentIsNullAndDeletedAtIsNullOrderByCreatedAtAsc(boardId);

        return topLevelComments.stream()
                .map(CommentDto::fromEntity)
                .toList();
    }

    public Comment getCommentById(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public Boolean checkAuthorization(UUID commentId, Member member) {
        Comment comment = getCommentById(commentId);
        Member commentOwner = comment.getBoard().getMember();
        return commentOwner.getId().equals(member.getId());
    }
}
