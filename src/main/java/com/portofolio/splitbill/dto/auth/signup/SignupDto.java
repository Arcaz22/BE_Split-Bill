package com.portofolio.splitbill.dto.auth.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.portofolio.splitbill.dto.validation.ValidUnique;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDto {
    @NotBlank(message = "Username is required")
    @ValidUnique(column = "username", message = "Username already been taken")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @JsonProperty("confirm_password")
    private String confirmPassword;

    public void setUsername(String username) {
        this.username = username != null ? username.toLowerCase() : null;
    }

    @AssertTrue(message = "Password and Confirm Password must be the same")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
