package com.tinka.common.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter; import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "tinka.kafka.topics")
@Getter @Setter
public class TopicProperties {

    private Auth auth;
    private Products products;
    private Orders orders;
    private Payments payments;
    private Reviews reviews;
    private Notification notification;
    private Audit audit;

    @Getter @Setter public static class Auth {
        @NotBlank private String userCreated;
        @NotBlank private String userUpdated;
        @NotBlank private String userDeleted;
        @NotBlank private String sellerVerified;
    }
    @Getter @Setter public static class Products {
        @NotBlank private String created;
        @NotBlank private String updated;
        @NotBlank private String deleted;
        @NotBlank private String verified;
        @NotBlank private String outOfStock;   // maps from 'out-of-stock' in YAML
    }
    @Getter @Setter public static class Orders {
        @NotBlank private String placed;
        @NotBlank private String confirmed;
        @NotBlank private String cancelled;
        @NotBlank private String shipped;
        @NotBlank private String delivered;
        @NotBlank private String failed;
    }
    @Getter @Setter public static class Payments {
        @NotBlank private String initiated;
        @NotBlank private String processed;
        @NotBlank private String failed;
        @NotBlank private String refundIssued;
    }
    @Getter @Setter public static class Reviews     { @NotBlank private String created; }
    @Getter @Setter public static class Notification{ @NotBlank private String requested; }
    @Getter @Setter public static class Audit       { @NotBlank private String adminActionLogged; }
}
