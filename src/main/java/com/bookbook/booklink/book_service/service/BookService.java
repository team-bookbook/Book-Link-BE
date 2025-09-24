package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.dto.request.BookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.repository.LibraryBookRepository;
import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.library_service.model.Library;
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
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryBookCopyService libraryBookCopyService;
    private final IdempotencyService idempotencyService;

    @Transactional
    public UUID registerLibraryBook(@Valid LibraryBookRegisterDto bookRegisterDto, String traceId, UUID userId) {
        log.info("[BookService] [traceId = {}, userId = {}] register library book initiate bookId={}", traceId, userId, bookRegisterDto.getId());

        // 멱등성 체크
        String key = "book:register:" + traceId;
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // register book
        Book book = bookRepository.findById(UUID.fromString(bookRegisterDto.getId()))
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        // todo : 유저 id 와 도서관 id 맵핑 후 library 엔티티 검색
        Library library = new Library(); // 임시

        LibraryBook libraryBook = LibraryBook.toEntity(bookRegisterDto, book, library);

        // todo : 1:N 유저 맵핑 후, 해당 유저가 해당 ISBN 코드로 책을 등록한 적 있는지 확인

        for(int i = 0; i < bookRegisterDto.getCopies(); i++) {
            libraryBookCopyService.registerLibraryBookCopy(libraryBook, traceId, userId);
        }

        LibraryBook savedLibraryBook = libraryBookRepository.save(libraryBook);
        UUID bookId = savedLibraryBook.getId();

        log.info("[BookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }
}
    