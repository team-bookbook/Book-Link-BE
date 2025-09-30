package com.bookbook.booklink.borrow_service.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.borrow_service.repository.BorrowRepository;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
}
    