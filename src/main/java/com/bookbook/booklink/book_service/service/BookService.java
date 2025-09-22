package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.dto.request.BookRegDto;
import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.event.RedisLockUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.book_service.repository.BookRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RedisLockUtil redisLockUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UUID registerLibrary(@Valid BookRegDto bookRegDto, String traceId, UUID userId) {
        log.info("[BookService] [traceId = {}, userId = {}] register book initiate name={}", traceId, userId, bookRegDto.getName());

        // 멱등성 체크
        String key = "book:register:" + traceId;
        redisLockUtil.acquireLockOrThrow(key);

        // todo : event publisher 구조 논의
        eventPublisher.publishEvent(LockEvent.builder().key(key).build());

        // register book
        Book newBook = Book.toEntity(bookRegDto);

        Book savedBook = bookRepository.save(newBook);
        UUID bookId = savedBook.getId();

        log.info("[BookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }
}
    