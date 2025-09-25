package com.bookbook.booklink.book_service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@Schema(description = "도서 등록 요청 DTO")
public class LibraryBookRegisterDto {
    @Schema(description = "도서 ID", example = "마흔에 읽는 쇼펜하우어", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "도서 ID는 필수입니다.")
    UUID id;

    @Schema(description = "보유 권수", example = "1000")
    @NotNull(message = "보유 권수는 필수입니다.")
    @Min(value = 0, message = "보유한 도서 개수는 양수여야 합니다.")
    Integer copies;

    @Schema(description = "보증금", example = "1000")
    @NotNull(message = "보증금 가격은 필수입니다.")
    @Min(value = 0, message = "보증금 가격은 양수여야 합니다.")
    Integer deposit;
}
