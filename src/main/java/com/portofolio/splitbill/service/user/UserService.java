package com.portofolio.splitbill.service.user;

import java.security.Principal;
import java.util.Map;

import com.portofolio.splitbill.dto.user.UserChangePasswordDto;
import com.portofolio.splitbill.dto.user.UserChangeProfileDto;
import com.portofolio.splitbill.dto.user.UserProfileResponseDto;

public interface UserService {
    public UserProfileResponseDto getUserProfile(Principal principal);

    public void changeProfile(Principal principal, UserChangeProfileDto userChangeProfileDto);

    public void changePassword(Principal principal, UserChangePasswordDto userChangePasswordDto);

    public Map<String, Object> getAllUsers(int page, int size, String username);
}
