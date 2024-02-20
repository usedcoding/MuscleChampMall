package com.example.MCM.domain.member.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.member.MemberRole;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 조회
    public Member getMember(String username) {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        if (member.isPresent()) {
            return member.get();
        }
        throw new DataNotFoundException("member not found");
    }

    //회원가입
    public Member create(String username, String password, String email, String nickname, String phoneNumber) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .role(MemberRole.USER)
                .createDate(LocalDateTime.now())
                .build();
        this.memberRepository.save(member);

        if (username.startsWith("admin")) {
            member = member.toBuilder()
                    .role(MemberRole.ADMIN)
                    .build();
            this.memberRepository.save(member);
        }
        return member;
    }

    //비밀번호 변경
//    public Member updateMemberPassword(MemberPasswordUpdateDTO memberPasswordUpdateDTO, String username) {
//        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));
//
//        if (!passwordEncoder.matches(memberPasswordUpdateDTO.getCurrentPassword(), member.getPassword())) {
//            return null;
//        } else {
//            memberPasswordUpdateDTO.setNewPassword(passwordEncoder.encode(memberPasswordUpdateDTO.getNewPassword()));
//            Member updatePassword = member.toBuilder()
//                    .password(memberPasswordUpdateDTO.getNewPassword())
//                    .build();
//            memberRepository.save(updatePassword);
//            return member;
//        }
//    }

    //소셜 로그인
    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return opMember.get();
        }

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(username, "", "", nickname, ""); // 최초 로그인 시 딱 한번 실행
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    //개인 정보 변경시 저장 메서드
    public Member updateMember(Member member, String password, String nickname, String email, String phoneNumber, String address) {
        Member updateMember = member.toBuilder()
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .modifyDate(LocalDateTime.now())
                .build();

        this.memberRepository.save(updateMember);

        return member;
    }

    //회원 삭제
    public Member delete(Member member) {
        Member memberDelete = member.toBuilder()
                .deleted(LocalDateTime.now())
                .isDeleted(true)
                .build();
        this.memberRepository.save(memberDelete);
        return member;
    }

    //비밀번호 찾기
    public Member findPassword(String email, String phoneNumber, String username) {

        return this.memberRepository.findByEmailAndPhoneNumberAndUsername(email, phoneNumber, username);
    }


    //아이디 찾기
    public Member findUsername(String email, String phoneNumber) {

        if(!email.isBlank()) {
            throw new DataNotFoundException("email not found");
        }

        if(!phoneNumber.isBlank()) {
            throw new DataNotFoundException("phoneNumber not found");
        }

        return this.memberRepository.findByEmailAndPhoneNumber(email, phoneNumber);
    }

    public Member findById(Long id) {
        Optional<Member> member = this.memberRepository.findById(id);
        if (member.isPresent()) {
            return member.get();
        }
        throw new DataNotFoundException("member not found");
    }

    public Page<Member> getAll(Pageable pageable) {
        return this.memberRepository.findAll(pageable);
    }

  public List<Member> getAll() {
        return this.memberRepository.findAll();
  }

}
