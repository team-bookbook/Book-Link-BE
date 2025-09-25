package com.bookbook.booklink.member.controller.docs;

import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.member.model.dto.request.SignUpReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/member")
@Tag(name = "Member API", description = "회원가입/회원정보 수정/삭제 관련 API")
public interface MemberApiDocs {

    // TODO
    // 어노테이션 인터페이스 분리 pr merge 시
    // 형식에 맞추어 변경 예정
    @Operation(
            summary = "회원 가입",
            description = "사용자로 부터 회원 정보를 입력 받아 가입합니다. "
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION,ErrorCode.PASSWORD_POLICY_INVALID})
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Boolean>> signup(
            @Valid @RequestBody SignUpReqDto signUpReqDto,
            @RequestHeader("Trace-Id") String traceId
    );
}
