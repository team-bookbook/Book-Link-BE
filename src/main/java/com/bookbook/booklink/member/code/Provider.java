package com.bookbook.booklink.member.code;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 가입 경로(계정자)")
public enum Provider {
    LOCAL,
    GOOGLE,
    KAKAO
}
