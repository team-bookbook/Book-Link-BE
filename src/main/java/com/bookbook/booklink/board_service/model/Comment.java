package com.bookbook.booklink.board_service.model;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.dto.request.CommentCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.CommentUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 댓글 엔티티
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    // 고유식별자
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    // 내용
    @Size(min = 1, max = 300)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // 좋아요수
    @Builder.Default
    private Integer likeCount = 0;

    // 작성자
    private UUID writerId;

    private String writerName;

    // 대댓글수
    @Builder.Default
    private Integer commentCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // 부모 댓글 (null이면 최상위 댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public static Comment toEntity(CommentCreateDto commentCreateDto, Member member, Comment parent) {
        return Comment.builder()
                .content(commentCreateDto.getContent())
                .writerName(member.getName())
                .writerId(member.getId())
                .parent(parent)
                .build();
    }

    public void update(CommentUpdateDto commentUpdateDto) {
        this.content = commentUpdateDto.getContent();
    }

    public void comment() {
        this.commentCount++;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void like() {
        this.likeCount++;
    }
}
