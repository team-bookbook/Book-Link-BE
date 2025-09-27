package com.bookbook.booklink.point_service.model;

import com.bookbook.booklink.point_service.model.dto.request.PointUseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "포인트 거래 내역 엔티티 - 포인트 사용, 충전, 교환, 환불 등 이력을 기록")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointHistory {

    @Schema(
            description = "거래 내역 고유 식별자 (UUID)",
            example = "3f4a9b9b-0e3c-4e7e-8a3c-5c9f5b3d2a19",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    @Schema(
            description = "거래 금액 (포인트 단위)",
            example = "5000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer amount;

    @Column(nullable = false)
    @Min(value = 0, message = "잔액은 양수여야합니다.")
    @Schema(
            description = "거래 후 잔액 (포인트 단위)",
            example = "15000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer balanceAfter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(
            description = "거래 유형 (예: CHARGE, USE, EXCHANGE, REFUND)",
            example = "USE",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private TransactionType type;

    @Column(nullable = false)
    @Size(max = 100)
    @Schema(
            description = "거래에 대한 설명",
            example = "책 구매를 위한 포인트 사용",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String description;

    @Column(nullable = false)
    @Schema(
            description = "거래 생성 시각 (자동 기록)",
            example = "2025-09-26T19:20:30",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    @Schema(
            description = "포인트를 얻거나 사용한 회원의 UUID",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID userId;

    public static PointHistory toEntity(PointUseDto pointUseDto, UUID userId) {
        return PointHistory.builder()
                .amount(pointUseDto.getAmount())
                .type(pointUseDto.getType())
                .description(pointUseDto.getType().getDefaultDescription())
                .userId(userId)
                .build();
    }

    public static PointHistory toEntity(Integer amount, TransactionType type, UUID userId) {
        return PointHistory.builder()
                .amount(amount)
                .type(type)
                .description(type.getDefaultDescription())
                .userId(userId)
                .build();
    }
}
