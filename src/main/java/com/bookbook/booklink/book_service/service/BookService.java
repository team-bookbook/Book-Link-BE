package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.dto.response.BookResponseDto;
import com.bookbook.booklink.book_service.repository.BookRepository;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final NationalLibraryService nationalLibraryService;
    private final ModelMapper modelMapper;

    @Transactional
    public BookResponseDto getBook(String isbn, String traceId, UUID userId) {
        log.info("[LibraryBookService] [traceId = {}, userId = {}] get book initiate isbn={}", traceId, userId, isbn);

        // 기존 데이터베이스에 도서 있는지 확인
        Book book = bookRepository.findByISBN(isbn);
        BookResponseDto result;
        if (book != null) {

        }


        // 없으면 api 호출
        try {
            BookResponseDto result = nationalLibraryService.searchBookByIsbn(isbn);
            return ResponseEntity.ok()
                    .body(BaseResponse.success(result));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.API_FALLBACK_FAIL);
            // todo : log 찍기
        }

        // 저장

        log.info("[LibraryBookService] [traceId = {}, userId = {}] get book initiate bookId={}", traceId, userId, bookId);

    }
}
