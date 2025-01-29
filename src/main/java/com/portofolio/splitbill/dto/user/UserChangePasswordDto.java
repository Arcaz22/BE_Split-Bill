package com.portofolio.splitbill.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserChangePasswordDto {

    @NotEmpty(message = "Old password is required")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotEmpty(message = "New password is required")
    @JsonProperty("new_password")
    private String newPassword;

    @NotEmpty(message = "Confirm password is required")
    @JsonProperty("confirm_password")
    private String confirmPassword;

}
