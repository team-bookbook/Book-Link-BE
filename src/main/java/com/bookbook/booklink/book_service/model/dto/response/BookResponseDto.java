package com.bookbook.booklink.book_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "도서 등록 요청 DTO")
public class BookResponseDto {
    @Schema(description = "도서 이름", example = "마흔에 읽는 쇼펜하우어", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "도서 이름은 필수입니다.")
    @Size(min = 1, max = 64, message = "도서관 이름은 1자 이상 64자 이하이어야 합니다.")
    String name;

    @Schema(description = "보증금", example = "1000")
    @Min(value = 0, message = "보증금 가격은 양수여야 합니다.")
    Integer deposit;

    @Schema(description = "정가", example = "17000")
    @Min(value = 0, message = "도서 정가는 양수여야 합니다.")
    Integer originalPrice;
}
