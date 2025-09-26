package com.bookbook.booklink.auth.controller.docs;

import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "인증 API", description = "JWT AccessToken, RefreshToken 관리 API")
@RequestMapping("/api/token")
public interface TokenApiDocs {

    @Operation(
            summary = "AccessToken 재발급",
            description = "RefreshToken을 이용해 새로운 AccessToken을 발급합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @RequestHeader("Refresh-Token") String refreshToken,
            @RequestHeader(value = "Trace-Id", required = false) String traceId
    );
}
