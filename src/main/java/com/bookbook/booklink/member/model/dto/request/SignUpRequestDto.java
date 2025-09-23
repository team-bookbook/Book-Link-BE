package com.bookbook.booklink.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "MemberSignUpDto", description = "회원가입 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @Email
    @NotBlank
    @Schema(description = "이메일 (로그인 ID)", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String email;

    @NotBlank @Size(min = 8, max = 100)
    @Schema(description = "비밀번호(평문 입력)", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 8, maxLength = 100)
    private String password;

    @NotBlank @Size(min = 2, max = 20)
    private String name;

    @NotBlank @Size(min = 2, max = 20)
    private String nickname;

    @Size(min = 5, max = 200)
    private String address;

    @Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$", message = "전화번호 형식은 01x-xxxx-xxxx 입니다.")
    private String phone;

    @Size(max = 500)
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", maxLength = 500)
    private String profile_image;

}
