package com.bookbook.booklink.book_service.controller;

import com.bookbook.booklink.book_service.controller.docs.BookApiDocs;
import com.bookbook.booklink.book_service.model.dto.request.BookRegisterDto;
import com.bookbook.booklink.common.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.book_service.service.BookService;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class BookController implements BookApiDocs {
    private final BookService bookService;

    @Override
    public ResponseEntity<BaseResponse<UUID>> registerBook(
            @Valid @RequestBody BookRegisterDto bookRegisterDto,
            @RequestHeader("Trace-Id") String traceId
            ) {
        UUID userId = UUID.randomUUID(); // todo : 실제 인증 정보에서 추출

        log.info("[BookController] [traceId = {}, userId = {}] register book request received, name={}",
                traceId, userId, bookRegisterDto.getName());

        UUID savedBookId = bookService.registerLibraryBook(bookRegisterDto, traceId, userId);

        log.info("[BookController] [traceId = {}, userId = {}] register book request success, bookId={}",
                traceId, userId, savedBookId);

        return ResponseEntity.ok()
                .body(BaseResponse.success(savedBookId));
    }
}
    