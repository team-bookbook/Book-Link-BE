package com.bookbook.booklink.book_service.controller;

import com.bookbook.booklink.book_service.controller.docs.LibraryBookApiDocs;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookUpdateDto;
import com.bookbook.booklink.book_service.service.LibraryBookService;
import com.bookbook.booklink.common.exception.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class LibraryBookController implements LibraryBookApiDocs {
    private final LibraryBookService bookService;

    @Override
    public ResponseEntity<BaseResponse<UUID>> registerLibraryBook(
            @Valid @RequestBody LibraryBookRegisterDto bookRegisterDto,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = UUID.randomUUID(); // todo : 실제 인증 정보에서 추출

        log.info("[LibraryBookController] [traceId = {}, userId = {}] register book request received, bookId={}",
                traceId, userId, bookRegisterDto.getId());

        UUID savedLibraryBookId = bookService.registerLibraryBook(bookRegisterDto, traceId, userId);

        log.info("[LibraryBookController] [traceId = {}, userId = {}] register book request success, libraryBookId={}",
                traceId, userId, savedLibraryBookId);

        return ResponseEntity.ok()
                .body(BaseResponse.success(savedLibraryBookId));
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> updateLibraryBook(
            @Valid @RequestBody LibraryBookUpdateDto updateBookDto,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = UUID.randomUUID(); // todo : 실제 인증 정보에서 추출

        log.info("[LibraryBookController] [traceId = {}, userId = {}] update library book request received, libraryBookId={}",
                traceId, userId, updateBookDto.getId());

        bookService.updateLibraryBook(updateBookDto, traceId, userId);

        log.info("[LibraryBookController] [traceId = {}, userId = {}] update library book request success, libraryBookId={}",
                traceId, userId, updateBookDto.getId());

        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
    