package com.bookbook.booklink.auth_service.model;

import com.bookbook.booklink.auth_service.code.Provider;
import com.bookbook.booklink.auth_service.code.Role;
import com.bookbook.booklink.auth_service.code.Status;
import com.bookbook.booklink.auth_service.model.dto.request.SignUpReqDto;
import com.bookbook.booklink.auth_service.model.dto.request.UpdateReqDto;
import com.bookbook.booklink.board_service.model.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @Schema(description = "회원 고유 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    @Schema(description = "이메일 (로그인 ID)", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    @Column(nullable = false, length = 100)
    @Schema(description = "비밀번호(BCrypt 해시)", example = "$2a$10$...", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 8, maxLength = 100)
    private String password;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(nullable = false, length = 20)
    @Schema(description = "이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 20)
    private String name;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(nullable = false, length = 20)
    @Schema(description = "닉네임", example = "북북이", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 20)
    private String nickname;

    @Size(min = 5, max = 200)
    @Column(length = 200)
    @Schema(description = "주소", example = "서울시 강남구 역삼동 123-45", minLength = 5, maxLength = 200)
    private String address;

    @Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$",
            message = "전화번호 형식은 01x-xxxx-xxxx 입니다.")
    @Column(length = 20)
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "계정자(회원 가입 경로)", example = "LOCAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "역할", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "상태", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private Status status;

    @Min(0)
    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "보유 포인트", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pointBalance = 0;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    @Schema(description = "가입 일자", example = "2025-09-23T20:05:00")
    private LocalDateTime createdAt;

    @Schema(description = "탈퇴 일자(소프트 삭제)", example = "2025-10-01T09:30:00")
    private LocalDateTime deletedAt;

    @Size(max = 500)
    @Column(length = 500)
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", maxLength = 500)
    private String profileImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Board> boardList = new ArrayList<>();

    public static Member ofLocalSignup(SignUpReqDto req, String encodedPassword) {
        return Member.builder()
                .email(req.getEmail())
                .password(encodedPassword)
                .name(req.getName())
                .nickname(req.getNickname())
                .address(req.getAddress())
                .phone(req.getPhone())
                .profileImage(req.getProfileImage())
                .provider(Provider.LOCAL)
                .role(Role.CUSTOMER)
                .status(Status.ACTIVE)
                .pointBalance(0)
                .build();
    }

    /**
     * 회원 정보 수정
     * nickname, address, phone, profileImage 업데이트
     */
    public void updateMemberInfo(UpdateReqDto dto) {
        this.nickname = dto.getNickname();
        this.address = dto.getAddress();
        this.phone = dto.getPhone();
        this.profileImage = dto.getProfileImage();
    }
}
    