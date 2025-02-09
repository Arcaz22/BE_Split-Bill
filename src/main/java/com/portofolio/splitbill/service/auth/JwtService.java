package com.portofolio.splitbill.service.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String[] generateToken(UserDetails userDetails);

    public String generateToken(UserDetails userDetails, String refreshToken);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public boolean isRefreshTokenValid(String token, UserDetails userDetails);

    public String extractUsername(String token);

    public String extractRefreshUsername(String token);

    public void signOut(String token);
}
