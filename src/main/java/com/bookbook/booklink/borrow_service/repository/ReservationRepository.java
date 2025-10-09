package com.bookbook.booklink.borrow_service.repository;

import com.bookbook.booklink.borrow_service.model.Reservation;
import com.bookbook.booklink.borrow_service.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findFirstByLibraryBookIdAndStatusOrderByReservedAtAsc(UUID libraryBookId, ReservationStatus reservationStatus);
}