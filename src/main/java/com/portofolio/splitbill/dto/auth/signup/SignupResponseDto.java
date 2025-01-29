package com.portofolio.splitbill.dto.auth.signup;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SignupResponseDto {
    private String username;

    private String phone;

    private String full_name;

    @JsonIgnore
    private String password;
}
