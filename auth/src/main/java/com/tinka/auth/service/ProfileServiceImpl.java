package com.tinka.auth.service;

import com.tinka.auth.dto.UpdateUserRequest;
import com.tinka.auth.dto.UserDto;
import com.tinka.auth.entity.SellerProfile;
import com.tinka.auth.entity.User;
import com.tinka.auth.exception.NotFoundException;
import com.tinka.auth.kafka.AuthEventPublisher;
import com.tinka.common.events.auth.UserDeletedEvent;
import com.tinka.common.events.auth.UserUpdatedEvent;
import com.tinka.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final AuthEventPublisher authEventPublisher;

    @Override
    public UserDto getCurrentUser(String userEmail) {
        User user = getUserOrThrow(userEmail);
        return UserDto.from(user);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request, String userEmail) {
        User user = getUserOrThrow(userEmail);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLanguage(request.getLanguage());

        // Optional: update seller profile if present
        SellerProfile seller = user.getSellerProfile();
        if (seller != null) {
            seller.setBusinessName(request.getBusinessName());
            seller.setBusinessTaxId(request.getBusinessTaxId());
            seller.setBusinessAddress(request.getBusinessAddress());
            // Assuming cascade = ALL so saving user will save sellerProfile
        }

        userRepository.save(user);

        // --- Publish UserUpdatedEvent ---
        authEventPublisher.userUpdated(
                UserUpdatedEvent.builder()
                        .userId(String.valueOf(user.getId()))
                        .email(user.getEmail())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .role(user.getRole().name())
                        .updatedAt(Instant.now())
                        .build()
        );

        return UserDto.from(user);
    }

    @Override
    public void changeLanguage(String userEmail, String language) {
        User user = getUserOrThrow(userEmail);
        user.setLanguage(language);
        userRepository.save(user);
        // Optionally publish event here if needed
    }

    @Override
    public void uploadAvatar(String userEmail, MultipartFile file) {
        User user = getUserOrThrow(userEmail);
        user.setProfileImageUrl(file.getOriginalFilename()); // Replace with file upload logic later
        userRepository.save(user);
        // Optionally publish event here if needed
    }

    @Override
    public void changeEmail(String userEmail, String newEmail) {
        User user = getUserOrThrow(userEmail);
        user.setEmail(newEmail);
        userRepository.save(user);
        // Optionally publish event here if needed
    }

    @Override
    @Transactional
    public void deleteUser(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        user.ifPresent(u -> {
            userRepository.delete(u);

            // --- Publish UserDeletedEvent ---
            authEventPublisher.userDeleted(
                    UserDeletedEvent.builder()
                            .userId(String.valueOf(u.getId()))
                            .deletedAt(Instant.now())
                            .build()
            );
        });
    }

    // ✅ Reusable private method
    private User getUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
