package com.tinka.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerRegisterRequest {
    // User fields
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    // SellerProfile fields
    private String businessName;
    private String businessEmail;
    private String businessTaxId;
    private String businessAddress;
    private String businessPhone;
    private String businessLogoUrl;
}
