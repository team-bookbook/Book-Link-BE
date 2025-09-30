package com.bookbook.booklink.library_service.controller.docs;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.library_service.model.dto.request.LibraryRegDto;
import com.bookbook.booklink.library_service.model.dto.request.LibraryUpdateDto;
import com.bookbook.booklink.library_service.model.dto.response.LibraryDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Library API", description = "도서관 등록/조회/수정 관련 API")
@RequestMapping("/api/library")
public interface LibraryApiDocs {

    @Operation(
            summary = "도서관 등록",
            description = "사용자 계정에 새로운 도서관을 등록합니다. " +
                    "하나의 계정당 도서관은 하나만 등록 가능합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION, ErrorCode.EMAIL_ALREADY_EXISTS})
    @PostMapping
    ResponseEntity<BaseResponse<UUID>> registerLibrary(
            @Valid @RequestBody LibraryRegDto libraryRegDto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    );

    @Operation(
            summary = "도서관 수정",
            description = "사용자 계정에 등록된 도서관을 수정합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PutMapping
    ResponseEntity<BaseResponse<UUID>> updateLibrary(
            @Valid @RequestBody LibraryUpdateDto libraryUpdateDto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    );

    @Operation(
            summary = "도서관 삭제",
            description = "사용자 계정에 등록된 도서관을 삭제합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Boolean>> deleteLibrary(
            @PathVariable @NotNull(message = "수정할 도서관의 ID는 필수입니다.") UUID id,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    );

    @Operation(
            summary = "특정 도서관 조회",
            description = "특정 도서관의 상세 정보를 조회합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION, ErrorCode.LIBRARY_NOT_FOUND})
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<LibraryDetailDto>> getLibrary(
            @PathVariable @NotNull(message = "조회할 도서관의 ID는 필수입니다.") UUID id
    );

    @Operation(
            summary = "내 주변 3km 이내 도서관 조회",
            description = "위치 기반으로 도서관 리스트 반환"
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @GetMapping
    ResponseEntity<BaseResponse<List<LibraryDetailDto>>> getLibraries(
            @RequestParam Double lat,
            @RequestParam Double lng
    );
}
