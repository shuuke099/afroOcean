package com.tinka.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {


    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;

    private String language;
    private String gender;
    private String profileDescription;

    private String businessName;
    private String businessTaxId;
    private String businessAddress;

    // Address Fields (to save into Address entity)
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}

