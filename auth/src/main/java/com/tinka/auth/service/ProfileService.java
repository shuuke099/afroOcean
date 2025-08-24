package com.tinka.auth.service;

import com.tinka.auth.dto.UpdateUserRequest;
import com.tinka.auth.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserDto getCurrentUser(String userEmail);

    UserDto updateUser(UpdateUserRequest request, String userEmail);

    void changeLanguage(String userEmail, String language);

    void uploadAvatar(String userEmail, MultipartFile file);

    void changeEmail(String userEmail, String newEmail);

    void deleteUser(String userEmail);
}

