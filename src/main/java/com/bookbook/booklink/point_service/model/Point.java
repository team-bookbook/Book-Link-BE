package com.bookbook.booklink.point_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Schema(description = "포인트 엔티티 - 회원의 포인트 잔액을 관리하는 객체")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    @Schema(
            description = "회원 고유 식별자 (UUID)",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID userId;

    @Min(value = 0, message = "잔액은 양수여야합니다.")
    @Column(nullable = false)
    @Schema(
            description = "포인트 잔액",
            example = "10000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer balance;

    /**
     * 포인트 사용 (차감)
     *
     * @param usedPoint 사용된 포인트
     */
    public void usePoint(Integer usedPoint) {
        this.balance += usedPoint;
    }
}
