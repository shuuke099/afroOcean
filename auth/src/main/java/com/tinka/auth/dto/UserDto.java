package com.tinka.auth.dto;

import com.tinka.auth.entity.Address;
import com.tinka.auth.entity.SellerProfile;
import com.tinka.auth.entity.User;
import com.tinka.auth.enums.Plan;
import com.tinka.auth.enums.Role;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    // Basic Info
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;

    // Access & Role
    private Role role;
    private Plan plan;
    private boolean emailVerified;
    private boolean twoFactorEnabled;

    // Location & Language (from Address entity)
    private String language;
    private String country;
    private String city;
    private String address;

    // Seller / Business Info
    private String sellerStatus;       // e.g., PENDING, APPROVED, NONE
    private String businessName;
    private String businessTaxId;
    private String businessPhone;
    private String businessAddress;
    private String businessLogoUrl;
    private String businessEmail;

    // Multi-tenant Support
    private String tenantId;

    // Optional: Useful in admin UI or security logs
    private boolean active;
    private boolean blocked;

    public static UserDto from(User user) {
        SellerProfile seller = user.getSellerProfile(); // May be null
        Address address = Optional.ofNullable(user.getAddresses())
                .filter(list -> !list.isEmpty())
                .map(List::getFirst)
                .orElse(null);


        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .plan(user.getPlan())
                .emailVerified(user.isEmailVerified())
                .twoFactorEnabled(user.isTwoFactorEnabled())
                .language(user.getLanguage())
                .country(address != null ? address.getCountry() : null)
                .city(address != null ? address.getCity() : null)
                .address(address != null ? address.getStreet() : null)
                .sellerStatus(seller != null
                        ? (Boolean.TRUE.equals(seller.getVerified()) ? "APPROVED" : "PENDING")
                        : "NONE")
                .businessName(seller != null ? seller.getBusinessName() : null)
                .businessTaxId(seller != null ? seller.getBusinessTaxId() : null)
                .businessPhone(seller != null ? seller.getBusinessPhone() : null)
                .businessAddress(seller != null ? seller.getBusinessAddress() : null)
                .businessLogoUrl(seller != null ? seller.getBusinessLogoUrl() : null)
                .businessEmail(seller != null ? seller.getBusinessEmail() : null)
                .tenantId(user.getTenantId())
                .active(user.isActive())
                .blocked(user.isAccountLocked())
                .build();
    }
}
