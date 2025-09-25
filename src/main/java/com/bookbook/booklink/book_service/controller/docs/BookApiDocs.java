package com.bookbook.booklink.book_service.controller.docs;

import com.bookbook.booklink.book_service.model.dto.request.BookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.library_service.model.dto.request.LibraryRegDto;
import com.bookbook.booklink.library_service.model.dto.request.LibraryUpdateDto;
import com.bookbook.booklink.library_service.model.dto.response.LibraryDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Book API", description = "도서 등록/조회/수정 관련 API")
@RequestMapping("/api/book")
public interface BookApiDocs {

    @Operation(
            summary = "도서 등록",
            description = "도서관에 새로운 도서를 등록합니다. " +
                    "하나의 도서관당 동일 도서는 한 번만 등록 가능합니다."
    )
    @ApiErrorResponses({ErrorCode.INVALID_CATEGORY_CODE, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> registerBook(
            @Valid @RequestBody LibraryBookRegisterDto bookRegisterDto,
            @RequestHeader("Trace-Id") String traceId
    );
}
