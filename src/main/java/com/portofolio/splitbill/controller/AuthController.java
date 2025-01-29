package com.portofolio.splitbill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.splitbill.api.AuthApi;
import com.portofolio.splitbill.dto.auth.signin.SigninDto;
import com.portofolio.splitbill.dto.auth.signin.SigninResponseDto;
import com.portofolio.splitbill.dto.auth.signup.SignupDto;
import com.portofolio.splitbill.service.auth.AuthService;
import com.portofolio.splitbill.util.ApiResponseUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupDto signupDto) {
        return ApiResponseUtil.success(HttpStatus.CREATED, "Signup success", authService.signup(signupDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@Valid @RequestBody SigninDto signinDto) {
        SigninResponseDto response = authService.signin(signinDto);
        return ApiResponseUtil.success(HttpStatus.OK, "Signin success", response);
    }

    @PostMapping("/signout")
    public ResponseEntity<Object> signOut() {
        authService.signOut();
        return ApiResponseUtil.success(HttpStatus.OK, "Signout success");
    }
}
