package com.example.MCM.domain.member.controller;

import com.example.MCM.domain.member.dto.*;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(MemberCreateDTO memberCreateDTO) {
        return "member_form";
    }

    @PostMapping("/signup")
    public String signup(Model model, @Valid MemberCreateDTO memberCreateDTO, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "member_form";
        }

        if (!memberCreateDTO.getPassword1().equals(memberCreateDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
        }

        try {
            this.memberService.create(memberCreateDTO.getUsername(), memberCreateDTO.getPassword1(), memberCreateDTO.getEmail(), memberCreateDTO.getNickname(), memberCreateDTO.getPhoneNumber());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
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

    @GetMapping("/me/{username}")
    public String myPage(Model model, @PathVariable(value = "username")String username) {
       Member member = this.memberService.getMember(username);
        model.addAttribute("member", member);
        return "member_myPage";
    }


    //비밀번호 변경
    @PostMapping("/update/password")
    public String updatePassword(@Valid MemberPasswordUpdateDTO memberPasswordUpdateDTO, Authentication authentication, Model model) {
            // new password 비교
            if (!Objects.equals(memberPasswordUpdateDTO.getNewPassword(), memberPasswordUpdateDTO.getConfirmPassword())) {
                model.addAttribute("dto", memberPasswordUpdateDTO);
                model.addAttribute("differentPassword", "비밀번호가 같지 않습니다.");
                return "redirect:/member/update/Password";
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Member result = memberService.updateMemberPassword(memberPasswordUpdateDTO, userDetails.getUsername());

            // 현재 비밀번호가 틀렸을 경우
            if (result == null) {
                model.addAttribute("dto", memberPasswordUpdateDTO);
                model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
                return "redirect:/member/update/Password";
            }

            return "redirect:/member/me";
    }

    @PostMapping("/update/me")
    public String updateMe(@Valid MemberEmailUpdateDTO memberEmailUpdateDTO, @Valid MemberAddressUpdateDTO memberAddressUpdateDTO, @Valid MemberNicknameUpdateDTO memberNicknameUpdateDTO,
                           @Valid MemberPhoneNumUpdateDTO memberPhoneNumUpdateDTO, Principal principal) {

        Member member = this.memberService.getMember(principal.getName());

        if(memberEmailUpdateDTO.getNewEmail() != null) {
            member = member.toBuilder()
                    .email(memberEmailUpdateDTO.getNewEmail())
                    .build();
            this.memberService.saveMember(member);
        }

        if(memberAddressUpdateDTO.getNewAddress() != null) {
            member = member.toBuilder()
                    .address(memberAddressUpdateDTO.getNewAddress())
                    .build();
            this.memberService.saveMember(member);
        }

        if(memberNicknameUpdateDTO.getNewNickname() != null) {
            member = member.toBuilder()
                    .address(memberNicknameUpdateDTO.getNewNickname())
                    .build();
            this.memberService.saveMember(member);
        }

        if(memberPhoneNumUpdateDTO.getNewPhoneNumber() != null) {
            member = member.toBuilder()
                    .address(memberPhoneNumUpdateDTO.getNewPhoneNumber())
                    .build();
            this.memberService.saveMember(member);
        }

        return "redirect:/";

    }
}
