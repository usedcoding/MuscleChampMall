package com.example.MCM.domain.email;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MemberRepository memberRepository;

    private JavaMailSender mailSender;


    private PasswordEncoder passwordEncoder;



    //이메일과 전화번호 확인 pwFind에서 사용
    public Boolean emailCheck(String email, String phoneNumber) {
        Member member = this.memberRepository.findByEmail(email);
        if(member != null && member.getPhoneNumber().equals(phoneNumber) && member.isDeleted() == false) {
            return true;
        } else {
            return false;
        }
    }

    //임시 비밀번호 Dto에 저장 코드
    public MailDto createMailAndChangePassword(String email, String username){
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle(username+"님의 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시비밀번호 안내 관련 이메일 입니다." + "[" + username + "]" +"님의 임시 비밀번호는 "
                + str + " 입니다.");
        updatePassword(str,email);
        return dto;
    }

    //임시 비밀번호 회원 정보에 저장
    public void updatePassword(String str,String email){

        Member member = this.memberRepository.findByEmail(email);

        member = member.toBuilder()
                        .password(passwordEncoder.encode(str))
                                .build();

        memberRepository.save(member);
    }


    //임시 비밀번호 생성 코드
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }


}
