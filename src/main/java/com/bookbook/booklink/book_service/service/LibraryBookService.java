package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.BookStatus;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.LibraryBookCopy;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookUpdateDto;
import com.bookbook.booklink.book_service.repository.BookRepository;
import com.bookbook.booklink.book_service.repository.LibraryBookRepository;
import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.library_service.model.Library;
import com.bookbook.booklink.library_service.service.LibraryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryBookService {
    private final LibraryBookRepository libraryBookRepository;
    private final IdempotencyService idempotencyService;
    private final LibraryService libraryService;
    private final BookService bookService;

    @Transactional
    public UUID registerLibraryBook(LibraryBookRegisterDto bookRegisterDto, String traceId, UUID userId) {
        log.info("[LibraryBookService] [traceId = {}, userId = {}] register library book initiate bookId={}", traceId, userId, bookRegisterDto.getId());

        // 멱등성 체크
        String key = "library-book:register:" + traceId;
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // find book & library
        Book book = bookService.findById(bookRegisterDto.getId());
        Library library = libraryService.findById(userId);

        // todo : 에러났을 때 멱등성 체크 풀기
        LibraryBook libraryBook = LibraryBook.toEntity(bookRegisterDto, book, library);

        // todo : 1:N 유저 맵핑 후, 해당 유저가 해당 ISBN 코드로 책을 등록한 적 있는지 확인

        for (int i = 0; i < bookRegisterDto.getCopies(); i++) {
            libraryBook.addCopy();
        }

        LibraryBook savedLibraryBook = libraryBookRepository.save(libraryBook);
        UUID bookId = savedLibraryBook.getId();

        log.info("[LibraryBookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }

    @Transactional
    public void updateLibraryBook(LibraryBookUpdateDto updateBookDto, String traceId, UUID userId) {
        log.info("[LibraryBookService] [traceId = {}, userId = {}] update library book initiate updateBookDto={}", traceId, userId, updateBookDto);

        LibraryBook libraryBook = libraryBookRepository.findById(updateBookDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        if (updateBookDto.getCopies() != null) libraryBook.updateCopies(updateBookDto.getCopies());
        if (updateBookDto.getDeposit() != null) libraryBook.updateDeposit(updateBookDto.getDeposit());

        log.info("[LibraryBookService] [traceId = {}, userId = {}] update library book success libraryBook={}", traceId, userId, libraryBook);
    }
}
    