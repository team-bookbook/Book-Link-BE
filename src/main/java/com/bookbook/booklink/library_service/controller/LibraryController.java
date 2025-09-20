package com.bookbook.booklink.library_service.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.library_service.model.dto.request.LibraryRegDto;
import com.bookbook.booklink.library_service.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/library")
@Tag(name = "Library API", description = "도서관 등록/조회/수정 관련 API")
public class LibraryController {
    private final LibraryService libraryService;


    @Operation(
            summary = "도서관 등록",
            description = "사용자 계정에 새로운 도서관을 등록합니다. " +
                    "하나의 계정당 도서관은 하나만 등록 가능합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "도서관 등록 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "successResponse",
                                            value = BaseResponse.LIBRARY_SUCCESS_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "입력값 오류로 인한 예외",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "errorResponse",
                                            value = BaseResponse.LIBRARY_ERROR_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 오류로 인한 예외",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "errorResponse",
                                            value = BaseResponse.LIBRARY_ERROR_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> registerLibrary(
            @Valid @RequestBody LibraryRegDto libraryRegDto,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = UUID.randomUUID(); // todo: 실제 인증 정보에서 추출

        log.info("[LibraryController] [traceId = {}, userId = {}] register library request received, name={}",
                traceId, userId, libraryRegDto.getName());

        UUID savedLibraryId = libraryService.registerLibrary(libraryRegDto, traceId, userId);

        log.info("[LibraryController] [traceId = {}, userId = {}] register library response success, libraryId={}",
                traceId, userId, savedLibraryId);
        return ResponseEntity.ok()
                .body(BaseResponse.success(savedLibraryId));
    }

}
