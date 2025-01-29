package com.portofolio.splitbill.dto.auth.signup;

import com.portofolio.splitbill.dto.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetPasswordDto {
    @ValidPassword(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
