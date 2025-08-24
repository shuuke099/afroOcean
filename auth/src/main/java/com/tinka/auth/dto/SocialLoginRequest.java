package com.tinka.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageUrl;
    private String provider; // "GOOGLE" or "FACEBOOK"
}

