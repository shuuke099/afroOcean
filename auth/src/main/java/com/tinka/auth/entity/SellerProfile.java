package com.tinka.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfile extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessEmail;

    @Column(nullable = false)
    private String businessTaxId;

    @Column(nullable = false)
    private String businessAddress;

    private String businessPhone;

    private String businessLogoUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean verified = false;

    private LocalDateTime verifiedAt;

    private String verificationNotes;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}
