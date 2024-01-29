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

    private final JavaMailSender mailSender;

    private final PasswordEncoder passwordEncoder;


    //임시 비밀번호 생성 코드
    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String tempPw = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            tempPw += charSet[idx];
        }
        return tempPw;
    }

    //임시 비밀번호 회원 정보에 저장
    public void updatePassword(String tempPw, String email, String phoneNumber, String username) {

        Member member = this.memberRepository.findByEmailAndPhoneNumberAndUsername(email, phoneNumber, username);


        Member updateMember = member.toBuilder()
                .password(passwordEncoder.encode(tempPw))
                .build();

        memberRepository.save(updateMember);
    }


}
