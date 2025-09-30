package com.bookbook.booklink.board_service.repository;

import com.bookbook.booklink.board_service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByBoardIdAndParentIsNullAndDeletedAtIsNullOrderByCreatedAtAsc(UUID boardId);
}
    