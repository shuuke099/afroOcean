package com.tinka.auth.service;

import com.tinka.auth.dto.*;
import com.tinka.auth.entity.*;
import com.tinka.auth.enums.Plan;
import com.tinka.auth.enums.Role;
import com.tinka.auth.kafka.AuthEventPublisher;
import com.tinka.common.events.auth.UserCreatedEvent;
import com.tinka.common.events.auth.SellerVerifiedEvent;
import com.tinka.auth.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final AuthEventPublisher authEventPublisher;  // <-- Added for Kafka

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        String token = UUID.randomUUID().toString();
        String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + token;
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .registeredVia("EMAIL")
                .emailVerified(false)
                .emailVerificationToken(token)
                .accountLocked(false)
                .active(true)
                .deleted(false)
                .twoFactorEnabled(false)
                .plan(Plan.FREE)
                .tenantId("default")
                .language("en")
                .build();

        userRepository.save(user);

        // --- Publish UserCreatedEvent ---
        authEventPublisher.userCreated(
                UserCreatedEvent.builder()
                        .userId(String.valueOf(user.getId()))
                        .email(user.getEmail())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .role(user.getRole().name())
                        .createdAt(Instant.now())
                        .build()
        );

        mailService.sendVerificationEmail(user.getEmail(), verificationUrl);
        return generateAuthResponse(user, null);
    }

    @Override
    public AuthResponse registerSeller(SellerRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String token = UUID.randomUUID().toString();

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.SELLER)
                .emailVerified(false)
                .emailVerificationToken(token)
                .registeredVia("EMAIL")
                .accountLocked(false)
                .active(true)
                .deleted(false)
                .twoFactorEnabled(false)
                .plan(Plan.FREE)
                .tenantId("default")
                .language("en")
                .build();

        SellerProfile profile = SellerProfile.builder()
                .businessName(request.getBusinessName())
                .businessEmail(Optional.ofNullable(request.getBusinessEmail()).orElse(request.getEmail()))
                .businessTaxId(request.getBusinessTaxId())
                .businessAddress(request.getBusinessAddress())
                .businessPhone(request.getBusinessPhone())
                .businessLogoUrl(request.getBusinessLogoUrl())
                .verified(false)
                .user(user)
                .build();

        user.setSellerProfile(profile);
        userRepository.save(user);

        // --- Publish UserCreatedEvent ---
        authEventPublisher.userCreated(
                UserCreatedEvent.builder()
                        .userId(String.valueOf(user.getId()))
                        .email(user.getEmail())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .role(user.getRole().name())
                        .createdAt(Instant.now())
                        .build()
        );

        // --- Publish SellerVerifiedEvent (unverified at registration) ---
        authEventPublisher.sellerVerified(
                SellerVerifiedEvent.builder()
                        .userId(String.valueOf(user.getId()))
                        .sellerId(profile.getId())
                        .businessName(profile.getBusinessName())
                        .taxId(profile.getBusinessTaxId())
                        .region(profile.getBusinessAddress())
                        .verified(false)
                        .verifiedAt(null)
                        .build()
        );

        String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + token;
        mailService.sendVerificationEmail(user.getEmail(), verificationUrl);

        return generateAuthResponse(user, profile);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if (!user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email not verified");
        }

        return generateAuthResponse(user, user.getSellerProfile());
    }

    @Override
    public AuthResponse socialLogin(SocialLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .profileImageUrl(request.getProfileImageUrl())
                    .role(Role.USER)
                    .registeredVia(request.getProvider())
                    .emailVerified(true)
                    .accountLocked(false)
                    .active(true)
                    .deleted(false)
                    .twoFactorEnabled(false)
                    .plan(Plan.FREE)
                    .tenantId("default")
                    .language("en")
                    .build();
            userRepository.save(user);
        }

        return generateAuthResponse(user, user.getSellerProfile());
    }

    @Override
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token"));
        user.setEmailVerificationToken(null);
        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    @Override
    public void logout(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Placeholder for actual logout/token invalidation
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void enableTwoFactor(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setTwoFactorEnabled(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void disableTwoFactor(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setTwoFactorEnabled(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private AuthResponse generateAuthResponse(User user, SellerProfile profile) {
        Map<String, Object> claims = Map.of(
                "role", user.getRole().name(),
                "emailVerified", user.isEmailVerified(),
                "tenantId", user.getTenantId(),
                "language", user.getLanguage(),
                "twoFactorEnabled", user.isTwoFactorEnabled(),
                "plan", user.getPlan(),
                "sellerStatus", profile != null && profile.getVerified() ? "APPROVED" : "PENDING"
        );

        return AuthResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .token(jwtService.generateToken(user.getEmail(), claims))
                .build();
    }
}
