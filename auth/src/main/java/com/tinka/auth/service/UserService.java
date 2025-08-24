package com.tinka.auth.service;

import com.tinka.auth.dto.*;

public interface UserService {

    // Core Auth
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

    // Seller Registration
    AuthResponse  registerSeller(SellerRegisterRequest request);

    // Social Login
    AuthResponse socialLogin(SocialLoginRequest request);

    // Email Verification
    void verifyEmail(String token);
    void resendVerificationEmail(String email);

    // Forgot / Reset Password
    void initiatePasswordReset(String email);
    void resetPassword(ResetPasswordRequest request);
    void changePassword(ChangePasswordRequest request, String userEmail);

    // Profile & Session
    UserDto getCurrentUser(String userEmail);
    void logout(String userEmail);

    // Two-Factor Authentication
    void enableTwoFactor(String userEmail);
    void disableTwoFactor(String userEmail);
}
