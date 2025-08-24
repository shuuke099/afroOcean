package com.tinka.common.events.notification;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class NotificationRequestedEvent extends BaseMarketplaceEvent {

    private String recipientId;       // userId, sellerId, etc.
    private String userId;
    private String channel;           // EMAIL/SMS/PUSH
    private String recipientType;     // USER/SELLER/ADMIN
    private String notificationType;  // EMAIL/SMS/IN_APP
    private String templateId;        // reference to a notification template
    private Map<String, String> data; // placeholder key-values for template substitution
    private String triggeredBy;       // which event or service triggered this

    // ✅ Direct contact/content fields (optional convenience)
    private String email;
    private String subject;
    private String message;

    @Override
    public String getSource() {
        return "notification";
    }
}
