package com.portofolio.splitbill.dto.auth.signin;

import lombok.Data;

@Data
public class SigninResponseDto {
    private String username;

    private String fullName;

    private String phone;

    private String accessToken;

    private String refreshToken;
}
