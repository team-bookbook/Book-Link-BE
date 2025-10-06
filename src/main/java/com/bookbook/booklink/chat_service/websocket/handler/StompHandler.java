package com.bookbook.booklink.chat_service.websocket.handler;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.auth_service.repository.MemberRepository;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import com.bookbook.booklink.common.jwt.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    //TODO service 레이어 분리
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 연결
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            System.out.println("🚀 CONNECT 요청 들어옴, token = " + token);
            if (token == null) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (!jwtUtil.validateToken(token)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            // ✅ 토큰에서 사용자 정보 추출
            String email = jwtUtil.getUsername(token);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            CustomUserDetails userDetails = new CustomUserDetails(member);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            accessor.setUser(authentication); // Principal → CustomUserDetails
            accessor.getSessionAttributes().put("AUTH", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("✅ CONNECT 인증 성공: email=" + email + ", memberId=" + member.getId());

        }
        // ✅ SEND 단계 (메시지 전송 시 principal 보장)
        if (StompCommand.SEND.equals(accessor.getCommand()) && accessor.getUser() == null) {
            System.out.println("📩 SEND 요청 들어옴, accessor.getUser() = " + accessor.getUser());
            Object auth = accessor.getSessionAttributes().get("AUTH");
            System.out.println("🔄 세션에서 AUTH 복원 시도: " + auth);
            if (auth instanceof Authentication) {
                accessor.setUser((Authentication) auth);
                SecurityContextHolder.getContext().setAuthentication((Authentication) auth);

                System.out.println("✅ AUTH 복원 완료");
            }else{
                System.out.println("❌ AUTH 복원 실패");
            }
        }

        return message;
    }
}