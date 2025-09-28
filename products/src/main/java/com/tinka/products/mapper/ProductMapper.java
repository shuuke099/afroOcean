package com.tinka.products.mapper;

import com.tinka.products.dto.*;
import com.tinka.products.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    // --- Request → Entity (create) ---
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "slug", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "sellerId", ignore = true),
            @Mapping(target = "audit", ignore = true),
            @Mapping(target = "workflow", ignore = true),
            @Mapping(target = "analytics", ignore = true),
            @Mapping(target = "reviewSummary", ignore = true),
            @Mapping(target = "seo", source = "seo"),
            @Mapping(target = "marketing", source = "marketing"),
            @Mapping(target = "location", source = "location"),
            @Mapping(target = "shipping", source = "shipping")
    })
    Product toEntity(ProductRequest request);

    // --- Update (patch-like behavior) ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Product product, ProductRequest request);

    // --- Entity → Response ---
    @Mappings({
            @Mapping(target = "id", expression = "java(String.valueOf(product.getId()))"),
            @Mapping(target = "slug", source = "slug"),
            @Mapping(target = "status", expression = "java(product.getStatus() != null ? product.getStatus().name() : null)"),
            @Mapping(target = "translations", source = "translations"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "inventory", source = "inventory"),
            @Mapping(target = "media", source = "media"),
            @Mapping(target = "marketing", source = "marketing"),
            @Mapping(target = "analytics", source = "analytics"),
            @Mapping(target = "reviews", source = "reviewSummary"),
            @Mapping(target = "workflow", source = "workflow"),
            @Mapping(target = "audit.createdAt", source = "audit.createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
            @Mapping(target = "audit.updatedAt", source = "audit.updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
            @Mapping(target = "audit.publishedAt", source = "audit.publishedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
            @Mapping(target = "audit.deletedAt", source = "audit.deletedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
            @Mapping(target = "workflow.timestamp", source = "workflow.timestamp", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
            @Mapping(target = "shipping", source = "shipping"),
            @Mapping(target = "location", source = "location"),
            @Mapping(target = "attributes", source = "attributes"),
            @Mapping(target = "seo", source = "seo"),
            @Mapping(target = "price.amount", source = "price.amount", defaultValue = "0"),
            @Mapping(target = "price.originalPrice", source = "price.originalPrice", defaultValue = "0"),
            @Mapping(target = "inventory.quantity", source = "inventory.quantity", defaultValue = "0")
    })
    ProductResponse toResponse(Product product);
}
