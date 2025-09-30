package com.bookbook.booklink.board_service.model;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.dto.request.BoardCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.BoardUpdateDto;
import jakarta.persistence.*;
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

// 게시글 엔티티
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    // 고유식별자
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String writerName;

    // 제목
    private String title;

    // 내용
    @Lob
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // 좋아요수
    @Builder.Default
    private Integer likeCount = 0;

    // 댓글수
    @Builder.Default
    private Integer commentCount = 0;

    // 게시글 카테고리
    private BoardCategory category;

    // 조회수
    @Builder.Default
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    public static Board toEntity(BoardCreateDto boardCreateDto, Member member) {
        return Board.builder()
                .writerName(member.getName())
                .title(boardCreateDto.getTitle())
                .content(boardCreateDto.getContent())
                .category(boardCreateDto.getCategory())
                .member(member)
                .build();

    }

    public void update(BoardUpdateDto boardUpdateDto) {
        this.title = boardUpdateDto.getTitle();
        this.content = boardUpdateDto.getContent();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
    
    public void view() {
        this.viewCount++;
    }

    public void like() {
        this.likeCount++;
    }

    public void comment() {
        this.commentCount++;
    }
}
    