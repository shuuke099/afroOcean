package com.tinka.products.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Primary image URL is required")
    private String imageUrl;

    private List<@NotBlank(message = "Image URL cannot be blank") String> images;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Brand is required")
    private String brand;

    @Min(value = 0, message = "Quantity must be zero or more")
    private Integer quantity;

    private boolean featured;

    // Don't allow setting status directly from frontend — default to DRAFT or PENDING in service

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Currency is required")
    private String currency;
}
