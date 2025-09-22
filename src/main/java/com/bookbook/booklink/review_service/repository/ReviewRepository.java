package com.bookbook.booklink.review_service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.bookbook.booklink.review_service.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

}
    