package com.bookbook.booklink.book_service.controller.docs;

import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookUpdateDto;
import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Library Book API", description = "도서관에 등록된 도서 등록/조회/수정 관련 API")
@RequestMapping("/api/library-book")
public interface LibraryBookApiDocs {

    @Operation(
            summary = "도서 등록",
            description = "도서관에 새로운 도서를 등록합니다. " +
                    "하나의 도서관당 동일 도서는 한 번만 등록 가능합니다."
    )
    @ApiErrorResponses({ErrorCode.INVALID_CATEGORY_CODE, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> registerLibraryBook(
            @Valid @RequestBody LibraryBookRegisterDto bookRegisterDto,
            @RequestHeader("Trace-Id") String traceId
    );

    @Operation(
            summary = "도서관별 도서 수정",
            description = "도서관에 등록된 도서의 보증금, 보유 권수를 수정합니다."
    )
    @ApiErrorResponses({ErrorCode.INVALID_CATEGORY_CODE, ErrorCode.BOOK_NOT_FOUND,
            ErrorCode.NOT_ENOUGH_AVAILABLE_COPIES_TO_REMOVE, ErrorCode.LIBRARY_BOOK_COPIES_MISMATCH,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PatchMapping
    public ResponseEntity<BaseResponse<Void>> updateLibraryBook(
            @Valid @RequestBody LibraryBookUpdateDto updateBookDto,
            @RequestHeader("Trace-Id") String traceId
    );

    @Operation(
            summary = "도서관별 도서 삭제",
            description = "도서관에 등록된 도서를 삭제합니다."
    )
    @ApiErrorResponses({ErrorCode.INVALID_CATEGORY_CODE, // todo : 에러 코드 추가
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @DeleteMapping("/{libraryBookId}")
    public ResponseEntity<BaseResponse<Void>> deleteLibraryBook(
            @PathVariable @NotNull(message = "삭제할 도서관별 도서의 id는 필수입니다.") UUID libraryBookId,
            @RequestHeader("Trace-Id") String traceId
    );
}
