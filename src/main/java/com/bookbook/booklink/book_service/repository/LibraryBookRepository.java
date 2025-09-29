package com.bookbook.booklink.book_service.repository;

import com.bookbook.booklink.book_service.model.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, UUID> {

    @Query(value = """
        SELECT lb.*
        FROM library_book lb
        JOIN book b ON lb.book_id = b.id
        JOIN library l ON lb.library_id = l.id
        WHERE lb.deleted_at IS NULL
          AND l.deleted_at IS NULL
          AND (:bookName IS NULL OR b.title LIKE %:bookName%)
          AND (6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) *
               cos(radians(l.longitude) - radians(:lng)) +
               sin(radians(:lat)) * sin(radians(l.latitude)))) <= 3
        """,
            nativeQuery = true)
    Page<LibraryBook> findLibraryBooksBySearch(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("bookName") String bookName,
            Pageable pageable
    );

}
    