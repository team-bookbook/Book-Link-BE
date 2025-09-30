package com.bookbook.booklink.book_service.service;

import com.bookbook.booklink.book_service.model.Book;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.LibraryBookCopy;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookSearchReqDto;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookUpdateDto;
import com.bookbook.booklink.book_service.model.dto.response.LibraryBookListDto;
import com.bookbook.booklink.book_service.repository.LibraryBookRepository;
import com.bookbook.booklink.common.dto.PageResponse;
import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.library_service.model.Library;
import com.bookbook.booklink.library_service.model.dto.response.LibraryBookListProjection;
import com.bookbook.booklink.library_service.service.LibraryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        Library library = libraryService.findByUserId(userId);

        // todo : 에러났을 때 멱등성 체크 풀기
        LibraryBook libraryBook = LibraryBook.toEntity(bookRegisterDto, book, library);

        // todo : 1:N 유저 맵핑 후, 해당 유저가 해당 ISBN 코드로 책을 등록한 적 있는지 확인

        for (int i = 0; i < bookRegisterDto.getCopies(); i++) {
            libraryBook.addCopy();
        }

        library.addBook();

        LibraryBook savedLibraryBook = libraryBookRepository.save(libraryBook);
        UUID bookId = savedLibraryBook.getId();

        log.info("[LibraryBookService] [traceId = {}, userId = {}] register book success bookId={}", traceId, userId, bookId);

        return bookId;
    }

    @Transactional
    public void updateLibraryBook(LibraryBookUpdateDto updateBookDto, String traceId, UUID userId) {
        log.info("[LibraryBookService] [traceId = {}, userId = {}] update library book initiate updateBookDto={}", traceId, userId, updateBookDto);

        LibraryBook libraryBook = getLibraryBookOrThrow(updateBookDto.getId());

        if (updateBookDto.getCopies() != null) libraryBook.updateCopies(updateBookDto.getCopies());
        if (updateBookDto.getDeposit() != null) libraryBook.updateDeposit(updateBookDto.getDeposit());

        log.info("[LibraryBookService] [traceId = {}, userId = {}] update library book success libraryBook={}", traceId, userId, libraryBook);
    }

    @Transactional
    public void deleteLibraryBook(UUID libraryBookId, String traceId, UUID userId) {
        log.info("[LibraryBookService] [traceId = {}, userId = {}] delete library book initiate libraryBookId={}", traceId, userId, libraryBookId);

        LibraryBook libraryBook = getLibraryBookOrThrow(libraryBookId);

        libraryBook.softDelete();
        log.info("[LibraryBookService] [traceId = {}, userId = {}] delete library book success libraryBookId={}", traceId, userId, libraryBookId);
    }

    @Transactional(readOnly = true)
    public PageResponse<LibraryBookListDto> getLibraryBookList(LibraryBookSearchReqDto request, UUID userId) {
        int page = request.getPage();
        int size = request.getSize();
        int offset = page * size;
        Double lat = request.getLatitude();
        Double lng = request.getLongitude();

        List<LibraryBookListProjection> projections =
                libraryBookRepository.findLibraryBooksBySearch(lat, lng, request.getBookName(), request.getSortType().toString(), size, offset);

        long total = libraryBookRepository.countLibraryBooksBySearch(lat, lng, request.getBookName());


        List<LibraryBookListDto> dtoList = projections.stream()
                .map(p -> LibraryBookListDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .author(p.getAuthor())
                        .libraryName(p.getLibraryName())
                        .distance(p.getDistance())
                        .copies(p.getCopies())
                        .borrowedCount(p.getBorrowedCount())
                        .deposit(p.getDeposit())
                        .rentedOut(p.getRentedOut() != null && p.getRentedOut() == 1)
                        .expectedReturnDate(p.getExpectedReturnDate())
                        .imageUrl(p.getImageUrl())
                        .build())
                .toList();

        return PageResponse.<LibraryBookListDto>builder()
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / size))
                .currentPage(page)
                .pageSize(size)
                .content(dtoList)
                .hasNext(offset + dtoList.size() < total)
                .hasPrevious(page > 0)
                .build();
    }

    /**
     * 특정 도서관의 Top 5 도서 목록을 반환하는 메서드
     *
     * @param libraryId 조회할 도서관의 ID
     * @return 해당 도서관의 가장 인기가 많은 도서 5개 리스트
     */
    @Transactional(readOnly = true)
    public List<LibraryBook> findTop5Books(UUID libraryId) {
        return libraryBookRepository.findTop5BooksByLibraryOrderByLikeCount(libraryId, PageRequest.of(0, 5));
    }

    public LibraryBook getLibraryBookOrThrow(UUID libraryBookId) {
        return libraryBookRepository.findById(libraryBookId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
    }

    public LibraryBookCopy getLibraryBookCopy(UUID libraryBookId) {
        LibraryBook libraryBook = getLibraryBookOrThrow(libraryBookId);
        LibraryBookCopy libraryBookCopy = null;
        for(LibraryBookCopy copy : libraryBook.getCopiesList()) {
            if (copy.getStatus().toString().equals("AVAILABLE")) {
                libraryBookCopy = copy;
            }
        }
        if(libraryBookCopy == null) {
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
        return libraryBookCopy;
    }
}
    