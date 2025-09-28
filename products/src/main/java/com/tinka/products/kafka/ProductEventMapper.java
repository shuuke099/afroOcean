package com.tinka.products.kafka;

import com.tinka.common.events.products.*;
import com.tinka.products.entity.*;
import java.time.ZoneOffset;

public class ProductEventMapper {

    private ProductEventMapper() {
        // Utility class
    }

    public static ProductCreatedEvent toCreatedEvent(Product product, String sellerId) {
        return ProductCreatedEvent.builder()
                .productId(String.valueOf(product.getId()))
                .sellerId(sellerId)
                .name(getName(product))
                .category(getCategory(product))
                .price(product.getPrice() != null ? product.getPrice().getAmount() : null)
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .createdAt(product.getAudit() != null && product.getAudit().getCreatedAt() != null
                        ? product.getAudit().getCreatedAt().toInstant(ZoneOffset.UTC) : null)
                .build();
    }

    public static ProductUpdatedEvent toUpdatedEvent(Product product, String sellerId) {
        return ProductUpdatedEvent.builder()
                .productId(String.valueOf(product.getId()))
                .sellerId(sellerId)
                .name(getName(product))
                .category(getCategory(product))
                .price(product.getPrice() != null ? product.getPrice().getAmount() : null)
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .updatedAt(product.getAudit() != null && product.getAudit().getUpdatedAt() != null
                        ? product.getAudit().getUpdatedAt().toInstant(ZoneOffset.UTC) : null)
                .build();
    }

    public static ProductDeletedEvent toDeletedEvent(Product product, String sellerId) {
        return ProductDeletedEvent.builder()
                .productId(String.valueOf(product.getId()))
                .sellerId(sellerId)
                .deletedAt(product.getAudit() != null && product.getAudit().getDeletedAt() != null
                        ? product.getAudit().getDeletedAt().toInstant(ZoneOffset.UTC) : null)
                .build();
    }

    public static ProductVerifiedEvent toVerifiedEvent(Product product, String sellerId) {
        return ProductVerifiedEvent.builder()
                .productId(String.valueOf(product.getId()))
                .verifiedBy(sellerId)
                .verified(product.getStatus() == ProductStatus.PUBLISHED)
                .verifiedAt(product.getAudit() != null && product.getAudit().getPublishedAt() != null
                        ? product.getAudit().getPublishedAt().toInstant(ZoneOffset.UTC) : null)
                .build();
    }

    public static ProductOutOfStockEvent toOutOfStockEvent(Product product, String sellerId) {
        return ProductOutOfStockEvent.builder()
                .productId(String.valueOf(product.getId()))
                .sellerId(sellerId)
                .availableQuantity(product.getInventory() != null && product.getInventory().getQuantity() != null
                        ? product.getInventory().getQuantity() : 0)
                .build();
    }

    // --- Helper methods ---
    private static String getName(Product product) {
        return (product.getTranslations() != null && !product.getTranslations().isEmpty())
                ? product.getTranslations().getFirst().getName()
                : null;
    }

    private static String getCategory(Product product) {
        return (product.getTranslations() != null && !product.getTranslations().isEmpty())
                ? product.getTranslations().getFirst().getCategory()
                : null;
    }
}
