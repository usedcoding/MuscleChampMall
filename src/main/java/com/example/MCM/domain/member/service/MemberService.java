package com.example.MCM.domain.member.service;

import com.example.MCM.domain.member.dto.MemberPasswordUpdateDTO;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMember(String username) {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        return member.get();
    }

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
    public Member updateMemberPassword(MemberPasswordUpdateDTO memberPasswordUpdateDTO, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(memberPasswordUpdateDTO.getCurrentPassword(), member.getPassword())) {
            return null;
        } else {
            memberPasswordUpdateDTO.setNewPassword(passwordEncoder.encode(memberPasswordUpdateDTO.getNewPassword()));
            member = member.toBuilder()
                            .password(memberPasswordUpdateDTO.getNewPassword())
                                    .build();
            memberRepository.save(member);
            return member;
        }
    }

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

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }


    //개인정보 변경시 저장 메서드
    public void saveMember(Member member) {
        this.memberRepository.save(member);
    }

    public Member delete (Member member) {
        member = member.toBuilder()
                .deleted(LocalDateTime.now())
                .isDeleted(true)
                .build();
        this.memberRepository.save(member);
        return member;

    }
}
