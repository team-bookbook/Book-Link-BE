package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.dto.request.BookRegisterDto;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.library_service.event.LibraryLockEvent;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.book_service.repository.BookRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final IdempotencyService idempotencyService;

    @Transactional
    public UUID registerLibrary(@Valid BookRegisterDto bookRegisterDto, String traceId, UUID userId) {
        log.info("[BookService] [traceId = {}, userId = {}] register book initiate name={}", traceId, userId, bookRegisterDto.getName());

        // 멱등성 체크
        String key = "book:register:" + traceId;
        idempotencyService.checkIdempotency(key, 1,
                () -> LibraryLockEvent.builder().key(key).build());

        // register book
        Book newBook = Book.toEntity(bookRegisterDto);

        // todo : 1:N 유저 맵핑 후, 해당 유저가 해당 ISBN 코드로 책을 등록한 적 있는지 확인

        Book savedBook = bookRepository.save(newBook);
        UUID bookId = savedBook.getId();

        log.info("[BookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }
}
    