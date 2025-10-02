package com.bookbook.booklink.borrow_service.repository;

import com.bookbook.booklink.borrow_service.model.BorrowStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.bookbook.booklink.borrow_service.model.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {

    List<Borrow> findAllByStatusInAndDueAtBefore(List<BorrowStatus> borrowed, LocalDateTime now);

    List<Borrow> findAllByStatusAndBorrowedAtBefore(BorrowStatus borrowStatus, LocalDateTime threeDaysAgo);
}