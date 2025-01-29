package com.portofolio.splitbill.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.splitbill.api.UserApi;
import com.portofolio.splitbill.dto.user.UserChangePasswordDto;
import com.portofolio.splitbill.dto.user.UserChangeProfileDto;
import com.portofolio.splitbill.service.user.UserService;
import com.portofolio.splitbill.util.ApiResponseUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile(Principal principal) {
        return ApiResponseUtil.success(HttpStatus.OK, "User profile", userService.getUserProfile(principal));
    }

    @PatchMapping(value = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> changeProfile(Principal principal,
            @Valid @ModelAttribute UserChangeProfileDto userChangeProfileDto) {
        userService.changeProfile(principal, userChangeProfileDto);
        return ApiResponseUtil.success(HttpStatus.OK, "Profile has been changed");
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Object> changePassword(Principal principal, @Valid @RequestBody UserChangePasswordDto userChangePasswordDto) {
        log.info("Received request to change password: {}", userChangePasswordDto);
        userService.changePassword(principal, userChangePasswordDto);
        return ApiResponseUtil.success(HttpStatus.OK, "Password has been changed");
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String username) {
        Map<String, Object> usersData = userService.getAllUsers(page, size, username);
        return ApiResponseUtil.success(HttpStatus.OK, "List of users", usersData);
    }
}
