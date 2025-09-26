package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
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
        log.info("[LibraryBookService] [traceId = {}, userId = {}] register library book initiate bookId={}", traceId, userId, bookRegisterDto.getId());

        // 멱등성 체크
        String key = "library-book:register:" + traceId;
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // find book
        Book book = bookRepository.findById(bookRegisterDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        // todo : library 추가(userId로 libraryId find)
        // todo : 에러났을 때 멱등성 체크 풀기
        LibraryBook libraryBook = LibraryBook.toEntity(bookRegisterDto, book);

        // todo : 1:N 유저 맵핑 후, 해당 유저가 해당 ISBN 코드로 책을 등록한 적 있는지 확인

        for (int i = 0; i < bookRegisterDto.getCopies(); i++) {
            libraryBook.addCopy(LibraryBookCopy.toEntity());
        }

        LibraryBook savedLibraryBook = libraryBookRepository.save(libraryBook);
        UUID bookId = savedLibraryBook.getId();

        log.info("[LibraryBookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }

    public void updateLibraryBook(LibraryBookUpdateDto updateBookDto, String traceId, UUID userId) {
        log.info("[LibraryBookService] [traceId = {}, userId = {}] update library book initiate libraryBookId={}", traceId, userId, updateBookDto.getId());

        LibraryBook libraryBook = libraryBookRepository.findById(updateBookDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        // 개수가 변경되었다면, 그에 맞게 librarybookcopies 변경해주어야 함
        if (updateBookDto.getCopies() != null) updateCopies(libraryBook, updateBookDto.getCopies());
        if (updateBookDto.getDeposit() != null) updateDeposit(libraryBook, updateBookDto.getDeposit());

        log.info("[LibraryBookService] [traceId = {}, userId = {}] update library book success libraryBookId={}", traceId, userId, updateBookDto.getId());
    }

    private void updateCopies(LibraryBook libraryBook, int copies) {
        libraryBook.updateCopies(copies);
    }

    private void updateDeposit(LibraryBook libraryBook, int deposit) {
        libraryBook.updateDeposit(deposit);
    }
}
    