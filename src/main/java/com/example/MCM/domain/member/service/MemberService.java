package com.example.MCM.domain.member.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.member.dto.MemberPasswordUpdateDTO;
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

    //회원가입
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

    //비밀번호 변경
//    public void updateMemberPassword(MemberPasswordUpdateDTO memberPasswordUpdateDTO, String password) {
//
//        this.memberRepository.save()
//
//    }

    //소셜 로그인
    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return opMember.get();
        }

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(username,"","", nickname,""); // 최초 로그인 시 딱 한번 실행
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }


    public Member getMember(String name) {
        Optional<Member> member = this.memberRepository.findByUsername(name);
        if (member.isPresent()) {
            return member.get();
        } throw new DataNotFoundException("member not found");
    }
}
