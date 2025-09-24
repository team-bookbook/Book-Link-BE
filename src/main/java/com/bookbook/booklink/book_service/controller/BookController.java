package com.bookbook.booklink.book_service.controller;

import com.bookbook.booklink.book_service.model.dto.request.BookRegisterDto;
import com.bookbook.booklink.common.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.book_service.service.BookService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@Tag(name = "Book API", description = "도서 등록/조회/수정 관련 API")
public class BookController {
    private final BookService bookService;


    @Operation(
            summary = "도서 등록",
            description = "도서관에 새로운 도서를 등록합니다. " +
                    "하나의 도서관당 도서는 하나만 등록 가능합니다."
    )
    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> registerBook(
            @Valid @RequestBody BookRegisterDto bookRegisterDto,
            @RequestHeader("Trace-Id") String traceId
            ) {
        UUID userId = UUID.randomUUID(); // todo : 실제 인증 정보에서 추출

        log.info("[BookController] [traceId = {}, userId = {}] register book request received, name={}",
                traceId, userId, bookRegisterDto.getName());

        UUID savedBookId = bookService.registerLibrary(bookRegisterDto, traceId, userId);

        log.info("[BookController] [traceId = {}, userId = {}] register book request success, bookId={}",
                traceId, userId, savedBookId);

        return ResponseEntity.ok()
                .body(BaseResponse.success(savedBookId));
    }
}
    