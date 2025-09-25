package com.bookbook.booklink.book_service.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.bookbook.booklink.book_service.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    boolean existsByISBN(String isbn);

    Book findByISBN(String isbn);
}
    