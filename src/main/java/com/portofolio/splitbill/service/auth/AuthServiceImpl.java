package com.portofolio.splitbill.service.auth;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portofolio.splitbill.dto.auth.signin.SigninDto;
import com.portofolio.splitbill.dto.auth.signin.SigninResponseDto;
import com.portofolio.splitbill.dto.auth.signup.SignupDto;
import com.portofolio.splitbill.dto.auth.signup.SignupResponseDto;
import com.portofolio.splitbill.model.User;
import com.portofolio.splitbill.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest request;

    private final UserRepository userRepository;

    public AuthServiceImpl(
        ModelMapper modelMapper,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        HttpServletRequest request,
        UserRepository userRepository
    ) {
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.userRepository = userRepository;
    }
    @Override
    public SignupResponseDto signup(SignupDto signupDto) {
        User user = modelMapper.map(signupDto, User.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        User savedUser = userRepository.save(user);

        SignupResponseDto signupResponseDto = modelMapper.map(savedUser, SignupResponseDto.class);

        signupResponseDto.setPassword(null);

        return signupResponseDto;
    }

    @Override
    public SigninResponseDto signin(SigninDto signinDto) {
        User user = userRepository.findByUsername(signinDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        signinDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SigninResponseDto responseSigninDto = modelMapper.map(user, SigninResponseDto.class);
        String[] tokens = jwtService.generateToken(user);
        responseSigninDto.setAccessToken(tokens[0]);
        responseSigninDto.setRefreshToken(tokens[1]);

        return responseSigninDto;
    }

    @Override
    public void signOut() {
        jwtService.signOut(getAuthenticationToken());
    }

    private String getAuthenticationToken() {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : "";
    }
}
