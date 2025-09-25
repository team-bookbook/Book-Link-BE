package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.dto.response.BookResponseDto;
import com.bookbook.booklink.book_service.model.dto.response.NationalLibraryResponseDto;
import com.bookbook.booklink.book_service.repository.BookRepository;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

        Book book = bookRepository.findByISBN(isbn);
        if (book != null) {
            BookResponseDto dto = modelMapper.map(book, BookResponseDto.class);
            log.info("[BookService] [traceId={}, userId={}] found bookId={}", traceId, userId, dto.getId());
            return dto;
        }

        NationalLibraryResponseDto apiResponse;
        try {
            apiResponse = nationalLibraryService.searchBookByIsbn(isbn);
        } catch (Exception e) {
            log.error("[BookService] [traceId={}, userId={}] API 호출 실패 isbn={}", traceId, userId, isbn, e);
            throw new CustomException(ErrorCode.API_FALLBACK_FAIL);
        }

        if (apiResponse == null) {
            throw new CustomException(ErrorCode.INVALID_ISBN_CODE);
        }

        BookResponseDto result = saveBookFromApi(apiResponse);
        log.info("[LibraryBookService] [traceId = {}, userId = {}] get book success bookId={}", traceId, userId, result.getId());
        return result;
    }

    @Transactional
    protected BookResponseDto saveBookFromApi(NationalLibraryResponseDto apiResponse) {
        Book newBook = modelMapper.map(apiResponse, Book.class);
        Book savedBook = bookRepository.save(newBook);
        return modelMapper.map(savedBook, BookResponseDto.class);
    }
}
