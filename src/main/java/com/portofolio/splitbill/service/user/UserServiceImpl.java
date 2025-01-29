package com.portofolio.splitbill.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portofolio.splitbill.dto.user.UserChangePasswordDto;
import com.portofolio.splitbill.dto.user.UserChangeProfileDto;
import com.portofolio.splitbill.dto.user.UserProfileResponseDto;
import com.portofolio.splitbill.exception.BadRequestException;
import com.portofolio.splitbill.model.User;
import com.portofolio.splitbill.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(
        ModelMapper modelMapper,
        PasswordEncoder passwordEncoder,
        UserRepository userRepository
    ) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Value("${spring.upload.directory}")
    private String uploadDirectory;

    @Value("${avatar.base-url}")
    private String avatarBaseUrl;

    @Override
    public void changeProfile(Principal principal, UserChangeProfileDto userChangeProfileDto) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not registered"));

        user.setFullName(userChangeProfileDto.getFullName());
        user.setPhone(userChangeProfileDto.getPhone());

        MultipartFile avatar = userChangeProfileDto.getAvatar();
        if (avatar != null) {
            if(!avatar.getContentType().startsWith("image/")) {
                throw new BadCredentialsException("Only image file allowed");
            }

            try {
                String filename = UUID.randomUUID().toString() + "."
                    + FilenameUtils.getExtension(avatar.getOriginalFilename());

                Path uploadPath = Paths.get(uploadDirectory);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(filename);
                Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setAvatar("uploads/" + filename);
            } catch (Exception e) {
                throw new BadRequestException("Failed to upload avatar");
            }
        }

        userRepository.save(user);
    }

    // @Override
    // public UserProfileResponseDto getUserProfile(Principal principal) {
    //     User user = userRepository.findByUsername(principal.getName())
    //             .orElseThrow(() -> new UsernameNotFoundException("User not registered"));

    //     UserProfileResponseDto userProfile = modelMapper.map(user, UserProfileResponseDto.class);

    //     return userProfile;
    // }
    @Override
    public UserProfileResponseDto getUserProfile(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not registered"));

        UserProfileResponseDto userProfile = modelMapper.map(user, UserProfileResponseDto.class);

        if (user.getAvatar() != null) {
            String avatarUrl = avatarBaseUrl + user.getAvatar();
            userProfile.setAvatar(avatarUrl);
        }

        return userProfile;
    }

    @Override
    public void changePassword(Principal principal, UserChangePasswordDto userChangePasswordDto) {
        log.info("Received request to change password: {}", userChangePasswordDto);
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            log.info("User {} found. Verifying old password.", principal.getName());

            if (!passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
                log.warn("Incorrect old password provided for user {}", principal.getName());
                throw new BadCredentialsException("Old password is incorrect");
            }

            if (!userChangePasswordDto.getNewPassword().equals(userChangePasswordDto.getConfirmPassword())) {
                log.warn("New password and confirm password do not match for user {}", principal.getName());
                throw new BadCredentialsException("New password and confirm password do not match");
            }

            log.info("Passwords match. Proceeding to change password for user {}", principal.getName());

            user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
            userRepository.save(user);

            log.info("Password successfully changed for user {}", principal.getName());
        } catch (Exception e) {
            log.error("Error while changing password for user {}: {}", principal.getName(), e.getMessage(), e);
            throw new RuntimeException("An error occurred while changing password", e);
        }
    }

    // @Override
    // public Map<String, Object> getAllUsers(int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     Page<User> userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);

    //     List<UserProfileResponseDto> users = userPage.getContent().stream()
    //             .map(user -> modelMapper.map(user, UserProfileResponseDto.class))
    //             .collect(Collectors.toList());

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("totalItems", userPage.getTotalElements());
    //     response.put("totalPages", userPage.getTotalPages());
    //     response.put("currentPage", userPage.getNumber() + 1);
    //     response.put("items", users);

    //     return response;
    // }
    @Override
    public Map<String, Object> getAllUsers(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        if (username != null && !username.trim().isEmpty()) {
            userPage = userRepository.findAllByUsernameContainingIgnoreCaseOrderByCreatedAtDesc(
                username.trim(),
                pageable
            );
        } else {
            userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        List<UserProfileResponseDto> users = userPage.getContent().stream()
                .map(user -> {
                    UserProfileResponseDto dto = modelMapper.map(user, UserProfileResponseDto.class);
                    if (user.getAvatar() != null) {
                        dto.setAvatar(avatarBaseUrl + user.getAvatar());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        response.put("currentPage", userPage.getNumber() + 1);
        response.put("items", users);

        return response;
    }

}
