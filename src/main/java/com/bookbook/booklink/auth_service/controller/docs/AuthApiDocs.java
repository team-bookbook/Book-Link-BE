package com.bookbook.booklink.auth_service.controller.docs;

import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
@Tag(name = "로그아웃 API", description = "로그아웃관련 RefreshToken 제거 API")
public interface AuthApiDocs {

    @Operation(
            summary = "로그아웃",
            description = "현재 인증된 사용자의 Refresh Token을 삭제하여 로그아웃 처리합니다.",
            security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Boolean>> logout(CustomUserDetails user);

}
