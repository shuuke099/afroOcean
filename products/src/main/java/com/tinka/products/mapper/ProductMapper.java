package com.tinka.products.mapper;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // 🔁 ProductRequest → Product (for creating/updating)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "slug", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "verified", ignore = true),
            @Mapping(target = "sellerId", ignore = true),
            @Mapping(target = "publishedAt", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "averageRating", ignore = true),
            @Mapping(target = "totalReviews", ignore = true)
    })
    Product toEntity(ProductRequest request);

    // 🔁 Product → ProductResponse (for returning to frontend)
    ProductResponse toResponse(Product product);
}
