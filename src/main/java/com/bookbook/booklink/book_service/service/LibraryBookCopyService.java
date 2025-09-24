package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.LibraryBookCopy;
import com.bookbook.booklink.book_service.repository.LibraryBookCopyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryBookCopyService {
    private final LibraryBookCopyRepository libraryBookCopyRepository;

    @Transactional
    public void registerLibraryBookCopy(LibraryBook libraryBook, String traceId, UUID userId) {
        log.info("[LibraryBookCopyService] [traceId = {}, userId = {}] register copy of library book initiate libraryBookId={}", traceId, userId, libraryBook.getId());

        LibraryBookCopy libraryBookCopy = LibraryBookCopy.toEntity(libraryBook);
        LibraryBookCopy savedLibraryBookCopy = libraryBookCopyRepository.save(libraryBookCopy);
        UUID libraryBookCopyId = savedLibraryBookCopy.getId();

        log.info("[LibraryBookCopyService] [traceId = {}, userId = {}] register copy of library book success libraryBookId={}", traceId, userId, libraryBookCopyId);
    }
}
