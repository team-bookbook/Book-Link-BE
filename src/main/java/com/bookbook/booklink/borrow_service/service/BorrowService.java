package com.bookbook.booklink.borrow_service.service;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.auth_service.service.MemberService;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.LibraryBookCopy;
import com.bookbook.booklink.book_service.repository.LibraryBookRepository;
import com.bookbook.booklink.book_service.service.LibraryBookService;
import com.bookbook.booklink.borrow_service.model.Borrow;
import com.bookbook.booklink.borrow_service.model.BorrowStatus;
import com.bookbook.booklink.borrow_service.model.dto.request.BorrowRequestDto;
import com.bookbook.booklink.borrow_service.repository.BorrowRepository;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.point_service.model.TransactionType;
import com.bookbook.booklink.point_service.model.dto.request.PointUseDto;
import com.bookbook.booklink.point_service.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final MemberService memberService;
    private final LibraryBookService libraryBookService;
    private final PointService pointService;

    @Transactional
    public UUID borrowBook(UUID userId, String traceId, BorrowRequestDto borrowRequestDto) {
        log.info("[BorrowService] [traceId = {}, userId = {}] borrow book initiate borrowRequestDto={}", traceId, userId, borrowRequestDto);

        UUID libraryBookId = borrowRequestDto.getLibraryBookId();
        LocalDateTime borrowedAt = LocalDateTime.now();
        LocalDateTime dueAt = borrowRequestDto.getExpectedReturnDate();

        LibraryBook libraryBook = libraryBookService.getLibraryBookOrThrow(libraryBookId);
        LibraryBookCopy copy = libraryBookService.getLibraryBookCopy(libraryBookId);
        Member member = memberService.getMemberOrThrow(userId);

        Borrow borrow = Borrow.createBorrow(copy, member, borrowedAt, dueAt);
        libraryBook.borrowCopy(copy, borrowedAt, dueAt);

        int deposit = copy.getLibraryBook().getDeposit();
        if (deposit > 0) {
            PointUseDto dto = PointUseDto.builder()
                    .amount(deposit)
                    .type(TransactionType.USE)
                    .build();
            pointService.usePoint(dto, UUID.fromString(traceId), userId);
        }

        // todo : 1대1 채팅창 open

        Borrow savedBorrow = borrowRepository.save(borrow);
        UUID borrowId = savedBorrow.getId();

        log.info("[BorrowService] [traceId = {}, userId = {}] borrow book success borrowId={}", traceId, userId, borrowId);
        return borrowId;
    }

    @Transactional
    public void acceptBorrowConfirm(UUID userId, String traceId, UUID borrowId) {
        log.info("[BorrowService] [traceId = {}, userId = {}] accept borrow confirm initiate borrowId={}", traceId, userId, borrowId);

        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException(ErrorCode.BORROW_NOT_FOUND));

        UUID memberId = borrow.getMember().getId();
        UUID libraryOwnerId = borrow.getLibraryBookCopy().getLibraryBook().getLibrary().getMember().getId();
        if (!userId.equals(memberId) && !userId.equals(libraryOwnerId)) {
            throw new CustomException(ErrorCode.BORROW_FORBIDDEN);
        }

        borrow.setBorrowed();

        log.info("[BorrowService] [traceId = {}, userId = {}] accept borrow confirm success borrowId={}", traceId, userId, borrowId);

    }

    @Transactional
    public void suspendBorrow(UUID userId, String traceId, UUID borrowId) {
        log.info("[BorrowService] [traceId = {}, userId = {}] suspend borrow initiate borrowId={}", traceId, userId, borrowId);

        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException(ErrorCode.BORROW_NOT_FOUND));

        if (!(borrow.getStatus().equals(BorrowStatus.BORROWED)
                || borrow.getStatus().equals(BorrowStatus.EXTENDED)
                || borrow.getStatus().equals(BorrowStatus.OVERDUE))) {
            throw new CustomException(ErrorCode.INVALID_BORROW_STATUS);
        }

        UUID memberId = borrow.getMember().getId();
        UUID libraryOwnerId = borrow.getLibraryBookCopy().getLibraryBook().getLibrary().getMember().getId();
        if (!userId.equals(memberId) && !userId.equals(libraryOwnerId)) {
            throw new CustomException(ErrorCode.BORROW_FORBIDDEN);
        }

        borrow.suspendBorrow();

        log.info("[BorrowService] [traceId = {}, userId = {}] suspend borrow success borrowId={}", traceId, userId, borrowId);

    }

    @Transactional
    public void acceptReturnBookConfirm(UUID borrowId, String imageUrl, UUID userId, String traceId) {
        log.info("[BorrowService] [traceId = {}, userId = {}] return book confirm accept initiate borrowId={}", traceId, userId, borrowId);

        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException(ErrorCode.BORROW_NOT_FOUND));

        if (!(borrow.getStatus().equals(BorrowStatus.BORROWED)
        || borrow.getStatus().equals(BorrowStatus.EXTENDED)
        || borrow.getStatus().equals(BorrowStatus.OVERDUE))) {
            throw new CustomException(ErrorCode.INVALID_BORROW_STATUS);
        }

        UUID libraryOwnerId = borrow.getLibraryBookCopy().getLibraryBook().getLibrary().getMember().getId();
        if (!userId.equals(libraryOwnerId)) {
            throw new CustomException(ErrorCode.BORROW_FORBIDDEN);
        }

        LibraryBookCopy copy = borrow.getLibraryBookCopy();
        LibraryBook libraryBook = copy.getLibraryBook();

        borrow.returnBook(LocalDateTime.now(), imageUrl);
        libraryBook.returnCopy(copy);

        log.info("[BorrowService] [traceId = {}, userId = {}] return book confirm accept success borrowId={}", traceId, userId, borrowId);
    }
}
    