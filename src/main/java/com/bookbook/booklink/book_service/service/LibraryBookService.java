package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.LibraryBookCopy;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.repository.BookRepository;
import com.bookbook.booklink.book_service.repository.LibraryBookRepository;
import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryBookService {
    private final BookRepository bookRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final IdempotencyService idempotencyService;

    @Transactional
    public UUID registerLibraryBook(LibraryBookRegisterDto bookRegisterDto, String traceId, UUID userId) {
        log.info("[BookService] [traceId = {}, userId = {}] register library book initiate bookId={}", traceId, userId, bookRegisterDto.getId());

        // 멱등성 체크
        String key = "book:register:" + traceId;
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // register book
        Book book = bookRepository.findById(bookRegisterDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        // todo : library 추가(userId로 libraryId find)
        LibraryBook libraryBook = LibraryBook.toEntity(bookRegisterDto, book);

        // todo : 1:N 유저 맵핑 후, 해당 유저가 해당 ISBN 코드로 책을 등록한 적 있는지 확인

        for (int i = 0; i < bookRegisterDto.getCopies(); i++) {
            LibraryBookCopy copy = LibraryBookCopy.toEntity(libraryBook);
            libraryBook.addCopy(copy);
        }

        LibraryBook savedLibraryBook = libraryBookRepository.save(libraryBook);
        UUID bookId = savedLibraryBook.getId();

        log.info("[BookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }
}
    