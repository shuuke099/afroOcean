package com.tinka.reviews.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    @NotBlank
    private String productId;

    @NotBlank
    private String reviewerId;

    @NotBlank
    private String reviewerName;

    @NotNull
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private Double rating;

    @Size(max = 1000)
    private String comment;
}
