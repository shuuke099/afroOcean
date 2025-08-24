package com.tinka.common.events.auth;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserCreatedEvent extends BaseMarketplaceEvent {

    private String userId;
    private String email;
    private String fullName;
    private String role;
    private Instant createdAt;

    @Override
    public String getSource() {
        return "auth"; // Change this based on your actual service name
    }
}
