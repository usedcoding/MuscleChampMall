package com.example.MCM.domain.member.service;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void create(String username, String password, String email, String nickname, String phoneNumber) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .build();
        this.memberRepository.save(member);
    }
}
