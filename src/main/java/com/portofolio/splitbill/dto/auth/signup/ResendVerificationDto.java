package com.portofolio.splitbill.dto.auth.signup;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendVerificationDto {
    @NotBlank(message = "Username is required")
    private String username;
}
