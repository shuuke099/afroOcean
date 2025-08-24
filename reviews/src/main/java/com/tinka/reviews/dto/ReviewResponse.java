package com.tinka.reviews.dto;

import com.tinka.reviews.entity.ReviewStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private String productId;
    private String reviewerId;
    private String reviewerName;
    private Double rating;
    private String comment;
    private ReviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
