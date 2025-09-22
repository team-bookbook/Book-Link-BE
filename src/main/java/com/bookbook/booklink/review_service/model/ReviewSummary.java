package com.bookbook.booklink.review_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummary {

    @Id
    private String target_id;

    private TargetType target_type;

    @Column(nullable = false)
    private Integer total_count;

    @Column(nullable = false)
    private Long total_rating;

    @Column(nullable = false)
    private Double avg_rating;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    public void addReview(int rating) {
        this.total_count++;
        this.total_rating += rating;
        this.avg_rating = (double) total_rating / total_count;
    }

    public void updateReview(int oldRating, int newRating) {
        this.total_rating = this.total_rating - oldRating + newRating;
        this.avg_rating = (double) total_rating / total_count;
    }

    public void removeReview(int rating) {
        this.total_count--;
        this.total_rating -= rating;
        this.avg_rating = total_count > 0 ? (double) total_rating / total_count : 0.0;
    }
}
