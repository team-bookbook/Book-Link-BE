package com.bookbook.booklink.auth_service.controller;

import com.bookbook.booklink.auth_service.controller.docs.AuthApiDocs;
import com.bookbook.booklink.auth_service.service.AuthService;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @Override
    public ResponseEntity<BaseResponse<Boolean>> logout(@AuthenticationPrincipal CustomUserDetails user){
        authService.logout(user.getUsername()); // email이 담겨있음
        return ResponseEntity.ok(BaseResponse.success(true));
    }
}
