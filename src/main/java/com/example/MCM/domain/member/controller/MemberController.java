package com.example.MCM.domain.member.controller;

import com.example.MCM.domain.member.dto.MemberCreateDTO;
import com.example.MCM.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(MemberCreateDTO memberCreateDTO) {
        return"member_form";
    }

    @PostMapping("/signup")
    public String signup(Model model, @Valid MemberCreateDTO memberCreateDTO, BindingResult bindingResult) {


        if(bindingResult.hasErrors()) {
            return"member_form";
        }

        if(!memberCreateDTO.getPassword1().equals(memberCreateDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
        }

        try {
            this.memberService.create(memberCreateDTO.getUsername(), memberCreateDTO.getPassword1(), memberCreateDTO.getEmail(), memberCreateDTO.getNickname(), memberCreateDTO.getPhoneNumber());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}
