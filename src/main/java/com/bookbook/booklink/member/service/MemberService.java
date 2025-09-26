package com.bookbook.booklink.member.service;

import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.member.model.Member;
import com.bookbook.booklink.member.model.dto.request.SignUpReqDto;
import com.bookbook.booklink.member.model.dto.response.ProfileResDto;
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
     * @param signUpReqDto 회원 가입 정보 DTO
     * @param traceId         요청 멱등성 체크용 ID (클라이언트 전달)
     * @return 등록된 Member ID
     */
    @Transactional
    public Member signUp(SignUpReqDto signUpReqDto, String traceId) {

        log.info("[MemberService] [traceId={}] signup member initiate, name={}",
                traceId, signUpReqDto.getName());

        String key = idempotencyService.generateIdempotencyKey("member:signup", traceId);

        // Redis Lock으로 멱등성 체크
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(signUpReqDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String encodedPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        // Library 엔티티 생성 후 DB 저장
        Member newMember = Member.ofLocalSignup(signUpReqDto,encodedPassword);
        Member saveMember = memberRepository.save(newMember);

        log.info("[MemberService] [traceId={}] signup member success, name={}",
                traceId, saveMember.getName());

        return saveMember;
    }

    /**
     * 주어진 UUID로 Member 엔티티를 조회합니다.
     * <p>
     * DB에서 해당 ID의 Member가 존재하지 않을 경우 {@link CustomException}을 발생시킵니다.
     * 이 메서드는 여러 서비스 계층에서 공통적으로 사용할 수 있는
     * "회원 조회 유틸리티" 메서드 역할을 합니다.
     *
     * @param memberID 조회할 회원의 UUID
     * @return Member 엔티티
     * @throws CustomException {@link ErrorCode#USER_NOT_FOUND} - 회원이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public Member getMemberOrThrow(UUID memberID){
        return memberRepository.findById(memberID)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 로그인한 사용자의 프로필 정보를 조회합니다.
     * <p>
     * 내부적으로 {@link #getMemberOrThrow(UUID)}를 호출하여 Member 엔티티를 가져온 뒤,
     * {@link ProfileResDto} 형태로 변환하여 반환합니다.
     *
     * @param memberId 로그인한 회원의 UUID
     * @return 프로필 응답 DTO ({@link ProfileResDto})
     * @throws CustomException {@link ErrorCode#USER_NOT_FOUND} - 회원이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public ProfileResDto getMyProfile(UUID memberId) {
        Member member = getMemberOrThrow(memberId);
        return ProfileResDto.from(member);
    }
}
    