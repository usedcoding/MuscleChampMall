package com.example.MCM.domain.member.controller;

import com.example.MCM.domain.member.dto.*;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @GetMapping("/signup")
    public String signup(MemberCreateDTO memberCreateDTO) {
        return "member_form";
    }

    //회원가입
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

    //로그인
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    //마이페이지
    @GetMapping("/me/{username}")
    public String myPage(Model model, @PathVariable(value = "username")String username) {
       Member member = this.memberService.getMember(username);
       if(member.isDeleted() == true){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
       } else {
           model.addAttribute("member", member);
           return "member_myPage";
       }
    }


    //비밀번호 변경
    @PostMapping("/update/password")
    public String updatePassword(@Valid MemberPasswordUpdateDTO memberPasswordUpdateDTO, Principal principal, Model model) {
            // new password 비교
            if (!Objects.equals(memberPasswordUpdateDTO.getNewPassword(), memberPasswordUpdateDTO.getConfirmPassword())) {
                model.addAttribute("dto", memberPasswordUpdateDTO);
                model.addAttribute("differentPassword", "비밀번호가 같지 않습니다.");
                return "redirect:/member/update/Password";
            }
            Member result = memberService.updateMemberPassword(memberPasswordUpdateDTO, principal.getName());


            // 현재 비밀번호가 틀렸을 경우
            if (result == null) {
                model.addAttribute("dto", memberPasswordUpdateDTO);
                model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
                return "redirect:/member/update/Password";
            }

            return "redirect:/member/me";
    }

    //개인 정보 변경
    @PostMapping("/update/me")
    public String updateMe(@Valid MemberEmailUpdateDTO memberEmailUpdateDTO, @Valid MemberAddressUpdateDTO memberAddressUpdateDTO, @Valid MemberNicknameUpdateDTO memberNicknameUpdateDTO,
                           @Valid MemberPhoneNumUpdateDTO memberPhoneNumUpdateDTO,BindingResult bindingResult, Principal principal, Authentication authentication) {

        Member member = this.memberService.getMember(principal.getName());

        if(bindingResult.hasErrors()) {
            return"redirect:/update/me";
        }

        if(member.isDeleted() == true){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
        } else {

            //이메일 변경
            if (memberEmailUpdateDTO.getNewEmail() != null) {
                member = member.toBuilder()
                        .email(memberEmailUpdateDTO.getNewEmail())
                        .build();
                this.memberService.saveMember(member);
            }

            //주소 변경
            if (memberAddressUpdateDTO.getNewAddress() != null) {
                member = member.toBuilder()
                        .address(memberAddressUpdateDTO.getNewAddress())
                        .build();
                this.memberService.saveMember(member);
            }

            //닉네임 변경
            if (memberNicknameUpdateDTO.getNewNickname() != null) {
                member = member.toBuilder()
                        .address(memberNicknameUpdateDTO.getNewNickname())
                        .build();
                this.memberService.saveMember(member);
            }

            //전화번호 변경
            if (memberPhoneNumUpdateDTO.getNewPhoneNumber() != null) {
                member = member.toBuilder()
                        .address(memberPhoneNumUpdateDTO.getNewPhoneNumber())
                        .build();
                this.memberService.saveMember(member);
            }
            return "redirect:/";
        }
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable(value = "username") String username, Model model) {
        Member member = this.memberService.getMember(username);
        if(member.isDeleted() == true){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
        } else {
            model.addAttribute("member", member);
            return "member_delete";
        }
    }

    //회원 삭제
    @PostMapping("/delete/{username}")
    public String delete(@PathVariable(value = "username") String username, Principal principal, @Valid MemberDeleteDTO memberDeleteDTO, BindingResult bindingResult) {
        Member member = this.memberService.getMember(username);

        if(bindingResult.hasErrors()) {
            return String.format("redirect:/delete/%s", member.getUsername());
        }
        if(!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        if(member.isDeleted() == true){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
        } else {

            if (memberDeleteDTO.getConfirmPassword().equals(member.getPassword())) {
                this.memberService.delete(member);
            } else if (!memberDeleteDTO.getConfirmPassword().equals(member.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
            return "redirect:/";
        }
    }

    //비밀번호 찾기 페이지 이동
    @GetMapping("/findPassword")
    public String findPassword() {
        return "password_find";
    }

    //회원 아이디 찾기 이동
    @GetMapping("/findUsername")
    public String findUsername(Model model) {
        //DTO초기화 코드 없으면 DTO인식이 안됨
        model.addAttribute("memberFindUsernameDTO", new MemberFindUsernameDTO());
        return"username_find";
    }

    //회원 아이디 찾기
    @PostMapping("/findUsername")
    public String findUsername(@Valid MemberFindUsernameDTO memberFindUsernameDTO, Model model){

        //DTO에 있는 데이터 호출 후 저장
        Member member = this.memberService.findUsername(memberFindUsernameDTO.getEmail(), memberFindUsernameDTO.getPhoneNumber());

        model.addAttribute("member", member);

        return "username_get";
    }
}
