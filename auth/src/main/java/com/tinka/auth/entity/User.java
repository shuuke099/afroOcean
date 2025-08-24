package com.tinka.auth.entity;

import com.tinka.auth.enums.Plan;
import com.tinka.auth.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phoneNumber;

    private String profileImageUrl;

    @Column(name = "seller_status")
    private String sellerStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Plan plan = Plan.FREE;

    @Column(nullable = false)
    private boolean emailVerified;

    private boolean accountLocked;

    private int loginAttempts;

    private LocalDateTime lastLogin;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private String emailVerificationToken;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean blocked = false;

    @Builder.Default
    private boolean deleted = false;

    // "CUSTOMER", "SELLER"
    private String userType;

    @Column(name = "tenant_id")
    private String tenantId;

    // "EMAIL", "GOOGLE"
    private String registeredVia;

    @Builder.Default
    private boolean twoFactorEnabled = false;

    @Column(name = "language")
    private String language;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private SellerProfile sellerProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Coupon> coupons = new ArrayList<>();
}
