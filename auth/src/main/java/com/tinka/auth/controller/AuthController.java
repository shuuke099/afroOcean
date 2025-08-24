package com.tinka.auth.controller;

import com.tinka.auth.dto.*;
import com.tinka.auth.security.UserPrincipal;
import com.tinka.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/register-seller")
    public ResponseEntity<AuthResponse> registerSeller(@Valid @RequestBody SellerRegisterRequest request) {
        return ResponseEntity.ok(userService.registerSeller(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(userService.socialLogin(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.ok("Verification email sent");
    }

    @PostMapping("/reset-password/initiate")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset initiated");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        userService.changePassword(request, user.getEmail());
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userService.getCurrentUser(user.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserPrincipal user) {
        userService.logout(user.getEmail());
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/2fa/enable")
    public ResponseEntity<String> enable2FA(@AuthenticationPrincipal UserPrincipal user) {
        userService.enableTwoFactor(user.getEmail());
        return ResponseEntity.ok("2FA enabled");
    }

    @PostMapping("/2fa/disable")
    public ResponseEntity<String> disable2FA(@AuthenticationPrincipal UserPrincipal user) {
        userService.disableTwoFactor(user.getEmail());
        return ResponseEntity.ok("2FA disabled");
    }

    @GetMapping("/default")
    public ResponseEntity<String> defaultEndpoint() {
        return ResponseEntity.ok("Auth service is running and connected to config server.");
    }
}
