package com.bookbook.booklink.auth.service;

import com.bookbook.booklink.common.jwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenService refreshTokenService;

    public void logout(String email) {
        refreshTokenService.logout(email);
    }
}
