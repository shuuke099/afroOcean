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
public class UserUpdatedEvent extends BaseMarketplaceEvent {

    private String userId;
    private String email;
    private String fullName;
    private String role;
    private Instant updatedAt;

    @Override
    public String getSource() {
        return "auth-service";
    }
}
