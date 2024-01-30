package com.example.MCM.domain.email;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MailController {

    private final JavaMailSender mailSender;

    private final MailService mailService;

    //로그인 인증번호 발송
    @GetMapping("/mailCheck")
    @ResponseBody
    public int processMailCheck(@RequestParam(value = "email", required = false) String email) throws Exception {
        int mailKey = (int) ((Math.random() * (99999 - 10000 + 1)) + 10000);

        String from = "usedcoding@gmail.com";//보내는 이 메일주소
        String to = email;
        String title = "회원가입시 필요한 인증번호 입니다.";
        String content = "[인증번호] " + mailKey + " 입니다. <br/> 인증번호 확인란에 기입해주십시오.";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

        } catch (MessagingException e) {
            throw new DataNotFoundException("이메일 전송 중 오류가 발생했습니다.");
        }
        return mailKey;
    }


    //비밀번호 전송
    @PostMapping("/user/findPw/sendEmail")
    public String sendEmailForPw(@Valid MailDto mailDto) {

        String tempPw = mailService.getTempPassword();
        String from = "usedcoding@gmail.com";//보내는 이 메일주소
        String to = mailDto.getEmail();
        String title = "임시 비밀번호입니다.";
        String content = mailDto.getEmail() + "님의" + "[임시 비밀번호] " + tempPw + " 입니다. <br/> 접속한 후 비밀번호를 변경해주세요";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

            //일치 하지 않는 경우 오류 처리 필요
            this.mailService.updatePassword(tempPw, mailDto.getEmail(), mailDto.getPhoneNumber(), mailDto.getUsername());

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }

        return "redirect:/";
    }

}

