package com.bookbook.booklink.borrow_service.scheduler;

import com.bookbook.booklink.borrow_service.model.Borrow;
import com.bookbook.booklink.borrow_service.model.BorrowStatus;
import com.bookbook.booklink.borrow_service.repository.BorrowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowScheduler {

    private final BorrowRepository borrowRepository;

    /**
     * 매일 자정(00:00)에 실행
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void markOverdueBorrows() {
        LocalDateTime now = LocalDateTime.now();

        // 대여 상태가 BORROWED, EXTENDED인데 dueDate가 지난 것들 조회
        List<Borrow> overdueBorrows = borrowRepository.findAllByStatusInAndDueAtBefore(
                List.of(BorrowStatus.BORROWED, BorrowStatus.EXTENDED),
                now
        );

        for (Borrow borrow : overdueBorrows) {
            borrow.markOverdue();
        }
    }
}