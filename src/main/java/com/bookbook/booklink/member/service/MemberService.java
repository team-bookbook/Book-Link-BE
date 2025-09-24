package com.bookbook.booklink.member.service;

import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.member.model.Member;
import com.bookbook.booklink.member.model.dto.request.SignUpRequestDto;
import com.bookbook.booklink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdempotencyService idempotencyService;

    /**
     * 회원가입
     * <p>
     * 동일 traceId 요청 시 중복 처리 방지를 위해 Redis Lock 체크 수행
     *
     * @param signUpRequestDto 회원 가입 정보 DTO
     * @param traceId         요청 멱등성 체크용 ID (클라이언트 전달)
     * @return 등록된 Member ID
     */
    @Transactional
    public Member signUp(SignUpRequestDto signUpRequestDto, String traceId) {

        log.info("[MemberService] [traceId={}] signup member initiate, name={}",
                traceId, signUpRequestDto.getName());

        String key = idempotencyService.generateIdempotencyKey("member:signup", traceId);

        // Redis Lock으로 멱등성 체크
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // Library 엔티티 생성 후 DB 저장
        Member newMember = Member.ofLocalSignup(signUpRequestDto,encodedPassword);
        Member saveMember = memberRepository.save(newMember);

        log.info("[MemberService] [traceId={}] signup member success, name={}",
                traceId, saveMember.getName());

        return saveMember;
    }
}
    