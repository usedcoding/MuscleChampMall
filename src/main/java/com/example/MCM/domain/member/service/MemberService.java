package com.example.MCM.domain.member.service;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String password, String email, String nickname, String phoneNumber) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .createDate(LocalDateTime.now())
                .build();
        return  this.memberRepository.save(member);
    }

    public Member modify (String password, String email, String nickname, String phoneNumber) {
        Member member = Member.builder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .build();
        return this.memberRepository.save(member);

    }


    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return opMember.get();
        }

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(username,"","", nickname,""); // 최초 로그인 시 딱 한번 실행
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

}
