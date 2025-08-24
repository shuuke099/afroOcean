package com.tinka.auth.controller;

import com.tinka.auth.dto.UpdateUserRequest;
import com.tinka.auth.dto.UserDto;
import com.tinka.auth.service.ProfileService;
import com.tinka.auth.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(profileService.getCurrentUser(user.getEmail()));
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest request,
                                              @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(profileService.updateUser(request, user.getEmail()));
    }

    @PatchMapping("/change-language")
    public ResponseEntity<UserDto> changeLanguage(@AuthenticationPrincipal UserPrincipal user,
                                                  @RequestParam String language) {
        profileService.changeLanguage(user.getEmail(), language);
        return ResponseEntity.ok(profileService.getCurrentUser(user.getEmail()));
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<UserDto> uploadAvatar(@RequestParam("file") MultipartFile file,
                                                @AuthenticationPrincipal UserPrincipal user) {
        profileService.uploadAvatar(user.getEmail(), file);
        return ResponseEntity.ok(profileService.getCurrentUser(user.getEmail()));
    }

    @PatchMapping("/change-email")
    public ResponseEntity<UserDto> changeEmail(@AuthenticationPrincipal UserPrincipal user,
                                               @RequestParam String newEmail) {
        profileService.changeEmail(user.getEmail(), newEmail);
        return ResponseEntity.ok(profileService.getCurrentUser(newEmail));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserPrincipal user) {
        profileService.deleteUser(user.getEmail());
        return ResponseEntity.ok("User account deleted");
    }
}
