package com.bookbook.booklink.borrow_service.service;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.auth_service.service.MemberService;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.service.LibraryBookService;
import com.bookbook.booklink.borrow_service.model.Reservation;
import com.bookbook.booklink.borrow_service.repository.ReservationRepository;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final LibraryBookService libraryBookService;
    private final MemberService memberService;

    @Transactional
    public UUID reserveBook(UUID userId, String traceId, UUID libraryBookId) {
        log.info("[ReservationService] [traceId = {}, userId = {}] reserve book initiate libraryBookId={}", traceId, userId, libraryBookId);

        LibraryBook libraryBook = libraryBookService.getLibraryBookOrThrow(libraryBookId);

        if (libraryBook.getAvailableBooks() != 0) {
            throw new CustomException(ErrorCode.BORROW_BOOK_AVAILABLE);
        }

        Member member =  memberService.getMemberOrThrow(userId);

        // todo 현재 대여중인 이력이 있는지 검사

        Reservation reservation = Reservation.toEntity(member, libraryBook);

        Reservation savedReservation = reservationRepository.save(reservation);
        UUID reservationId = savedReservation.getId();

        log.info("[ReservationService] [traceId = {}, userId = {}] reserve book success reservationId={}", traceId, userId, reservationId);
        return reservationId;
    }
}
