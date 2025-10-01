package com.bookbook.booklink.borrow_service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.bookbook.booklink.borrow_service.model.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {

}
    