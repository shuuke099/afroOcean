package com.tinka.common.topics;

public final class KafkaTopics {

    // Auth & Users
    public static final String USER_CREATED = "user.created";
    public static final String USER_UPDATED = "user.updated";
    public static final String USER_DELETED = "user.deleted";
    public static final String SELLER_VERIFIED = "seller.verified";

    // Product
    public static final String PRODUCT_CREATED = "product.created";
    public static final String PRODUCT_UPDATED = "product.updated";
    public static final String PRODUCT_DELETED = "product.deleted";
    public static final String PRODUCT_VERIFIED = "product.verified";
    public static final String PRODUCT_OUT_OF_STOCK = "product.out_of_stock";

    // Review
    public static final String REVIEW_CREATED = "review.created";

    // Order
    public static final String ORDER_PLACED = "order.placed";
    public static final String ORDER_CONFIRMED = "order.confirmed";
    public static final String ORDER_CANCELLED = "order.cancelled";
    public static final String ORDER_SHIPPED = "order.shipped";
    public static final String ORDER_DELIVERED = "order.delivered";
    public static final String ORDER_FAILED = "order.failed";

    // Payment
    public static final String PAYMENT_INITIATED = "payment.initiated";
    public static final String PAYMENT_PROCESSED = "payment.processed";
    public static final String PAYMENT_FAILED = "payment.failed";
    public static final String REFUND_ISSUED = "refund.issued";

    // Notifications
    public static final String NOTIFICATION_REQUESTED = "notification.requested";

    // Audit
    public static final String ADMIN_ACTION_LOGGED = "admin.action.logged";

    private KafkaTopics() {
        // Prevent instantiation
    }
}
