package com.portofolio.splitbill.dto.user;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.portofolio.splitbill.dto.validation.ValidPhoneNumber;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserChangeProfileDto {
    @Schema(name = "fullName", description = "Full name of the user", example = "Chandra Arcychan Azfar", required = false)
    @JsonProperty("fullName")
    @Size(min = 1, max = 30, message = "Full name must be between 1 and 30 characters long")
    private String fullName;

    // @ValidUnique(column = "phone", message = "Phone already been taken")
    @ValidPhoneNumber(message = "Phone must be a valid phone number")
    @Schema(description = "User's phone number", example = "6285156692133", required = false)
    private String phone;

    @Parameter(description = "User avatar image", schema = @Schema(type = "string", format = "binary", required = false))
    private MultipartFile avatar;
}
