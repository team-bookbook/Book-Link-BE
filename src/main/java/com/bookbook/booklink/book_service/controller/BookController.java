package com.bookbook.booklink.book_service.controller;

import com.bookbook.booklink.book_service.controller.docs.BookApiDocs;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.response.BookResponseDto;
import com.bookbook.booklink.book_service.service.LibraryBookService;
import com.bookbook.booklink.book_service.service.NationalLibraryService;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class BookController implements BookApiDocs {
    private final NationalLibraryService nationalLibraryService;

    @Override
    public ResponseEntity<BaseResponse<BookResponseDto>> getBook(
            @PathVariable @NotNull(message = "조회할 도서의 ISBN 코드는 필수입니다.") String isbn,
            @RequestHeader("Trace-Id") String traceId
            ) {
        UUID userId = UUID.randomUUID(); // todo : 실제 인증 정보에서 추출

        log.info("[BookController] [traceId = {}, userId = {}] register book request received, isbn={}",
                traceId, userId, isbn);

        try {
            BookResponseDto result = nationalLibraryService.searchBookByIsbn(isbn);
            return ResponseEntity.ok()
                    .body(BaseResponse.success(result));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.API_FALLBACK_FAIL);
            // todo : log 찍기
        }

//        BookResponseDto book = null;

//        log.info("[BookController] [traceId = {}, userId = {}] register book request success, book={}",
//                traceId, userId, book);
//
//        return ResponseEntity.ok()
//                .body(BaseResponse.success(book));
    }
}
    