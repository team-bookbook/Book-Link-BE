package com.bookbook.booklink.book_service.repository;

import com.bookbook.booklink.book_service.model.LibraryBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, UUID> {

    @Query("SELECT lb " +
            "FROM LibraryBook lb " +
            "JOIN FETCH lb.book b " +
            "WHERE lb.library.id = :libraryId " +
            "ORDER BY b.likeCount DESC")
    List<LibraryBook> findTop5BooksByLibraryOrderByLikeCount(@Param("libraryId") UUID libraryId, Pageable pageable);


}
    