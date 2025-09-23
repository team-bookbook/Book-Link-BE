package com.bookbook.booklink.member.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.member.model.Member;
import com.bookbook.booklink.member.model.dto.request.SignUpRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.member.service.MemberService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name= "Member API", description = "회원가입/회원정보 수정/삭제 관련 API")
public class MemberController {

    private final MemberService memberService;

    // TODO
    // 어노테이션 인터페이스 분리 pr merge 시
    // 형식에 맞추어 변경 예정
    @Operation(
            summary = "회원 가입",
            description = "사용자로 부터 회원 정보를 입력 받아 가입합니다. ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "successResponse",
                                            value = BaseResponse.SUCCESS_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "입력값 오류로 인한 예외",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "errorResponse",
                                            value = BaseResponse.ERROR_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 오류로 인한 예외",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "errorResponse",
                                            value = BaseResponse.ERROR_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Member>> signup(
            @Valid @RequestBody SignUpRequestDto request,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId,
            @RequestHeader(value = "X-User-Id", required = false) UUID userId
    ) {
        Member saved = memberService.signUp(request, traceId, userId);
        return ResponseEntity.ok().body(BaseResponse.success(saved));
    }
}
    