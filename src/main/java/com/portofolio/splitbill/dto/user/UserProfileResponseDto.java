package com.portofolio.splitbill.dto.user;

import lombok.Data;

@Data
public class UserProfileResponseDto {
    private String id;

    private String avatar;

    private String username;

    private String fullName;

    private String phone;
}
