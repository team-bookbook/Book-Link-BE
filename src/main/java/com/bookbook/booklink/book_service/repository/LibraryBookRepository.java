package com.bookbook.booklink.book_service.repository;

import com.bookbook.booklink.book_service.model.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, UUID> {
}
    