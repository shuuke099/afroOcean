package com.tinka.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
