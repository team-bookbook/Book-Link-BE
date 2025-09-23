package com.bookbook.booklink.member.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
}
    