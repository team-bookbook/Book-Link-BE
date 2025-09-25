package com.bookbook.booklink.member.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.member.controller.docs.MemberApiDocs;
import com.bookbook.booklink.member.model.Member;
import com.bookbook.booklink.member.model.dto.request.SignUpRequestDto;
import com.bookbook.booklink.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;

    @Override
    public ResponseEntity<BaseResponse<Boolean>> signup(
            @Valid @RequestBody SignUpRequestDto request,
            @RequestHeader(value = "Trace-Id",required = false) String traceId
    ) {
        Member saved = memberService.signUp(request, traceId);
        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }
}
    