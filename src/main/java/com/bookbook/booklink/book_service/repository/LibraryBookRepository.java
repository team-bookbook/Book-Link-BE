package com.bookbook.booklink.book_service.repository;

import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.model.dto.request.LibraryBookSortType;
import com.bookbook.booklink.book_service.model.dto.response.LibraryBookListDto;
import com.bookbook.booklink.library_service.model.dto.response.LibraryBookListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, UUID> {

    // todo : image url 추가
    @Query(value = """
        SELECT
            lb.id AS id,
            l.name AS libraryName,
            b.title AS title,
            b.author AS author,
            lb.copies AS copies,
            lb.borrowed_count AS borrowedCount,
            lb.deposit AS deposit,
            (lb.copies = lb.borrowed_count) AS rentedOut,
            CASE
                WHEN lb.copies = lb.borrowed_count THEN MIN(CASE WHEN lbc.due_at >= NOW() THEN lbc.due_at END)
                ELSE NULL
            END AS expectedReturnDate,
            (6371 * acos(
                cos(radians(:lat)) * cos(radians(l.latitude)) *
                cos(radians(l.longitude) - radians(:lng)) +
                sin(radians(:lat)) * sin(radians(l.latitude))
            )) AS distance,
            NULL AS imageUrl
        FROM library_book lb
        JOIN book b ON lb.book_id = b.id
        JOIN library l ON lb.library_id = l.id
        JOIN library_book_copy lbc ON lb.id = lbc.library_book_id
        WHERE lb.deleted_at IS NULL
          AND (:bookName IS NULL OR b.title LIKE %:bookName%)
        GROUP BY lb.id, l.name, b.title, b.author, lb.copies, lb.borrowed_count, lb.deposit, l.latitude, l.longitude
        HAVING distance <= 3
        ORDER BY
            CASE WHEN :sortType = 'LATEST' THEN lb.created_at END DESC,
            CASE WHEN :sortType = 'MOST_BORROWED' THEN lb.borrowed_count END DESC,
            CASE WHEN :sortType = 'DISTANCE' THEN distance END ASC
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true)
    List<LibraryBookListProjection> findLibraryBooksBySearch(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("bookName") String bookName,
            @Param("sortType") String sortType,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = """
    SELECT COUNT(DISTINCT lb.id)
    FROM library_book lb
    JOIN book b ON lb.book_id = b.id
    JOIN library l ON lb.library_id = l.id
    JOIN library_book_copy lbc ON lb.id = lbc.library_book_id
    WHERE lb.deleted_at IS NULL
      AND (:bookName IS NULL OR b.title LIKE %:bookName%)
      AND (6371 * acos(
            cos(radians(:lat)) * cos(radians(l.latitude)) *
            cos(radians(l.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(l.latitude))
          )) <= 3
    """,
            nativeQuery = true)
    long countLibraryBooksBySearch(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("bookName") String bookName
    );

    @Query("SELECT lb " +
            "FROM LibraryBook lb " +
            "JOIN FETCH lb.book b " +
            "WHERE lb.library.id = :libraryId " +
            "ORDER BY b.likeCount DESC")
    List<LibraryBook> findTop5BooksByLibraryOrderByLikeCount(@Param("libraryId") UUID libraryId, Pageable pageable);

}
    