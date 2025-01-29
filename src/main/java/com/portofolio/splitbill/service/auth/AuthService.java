package com.portofolio.splitbill.service.auth;

import com.portofolio.splitbill.dto.auth.signin.SigninDto;
import com.portofolio.splitbill.dto.auth.signin.SigninResponseDto;
import com.portofolio.splitbill.dto.auth.signup.SignupDto;
import com.portofolio.splitbill.dto.auth.signup.SignupResponseDto;

public interface AuthService {
    public SignupResponseDto signup(SignupDto signupDto);

    public SigninResponseDto signin(SigninDto signinDto);

    public void signOut();
}
