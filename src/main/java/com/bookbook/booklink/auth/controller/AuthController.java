package com.bookbook.booklink.auth.controller;

import com.bookbook.booklink.auth.controller.docs.AuthApiDocs;
import com.bookbook.booklink.auth.service.AuthService;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @Override
    public ResponseEntity<BaseResponse<Boolean>> logout(@AuthenticationPrincipal Member user){
        authService.logout(user.getEmail());
        return ResponseEntity.ok(BaseResponse.success(true));
    }
}
