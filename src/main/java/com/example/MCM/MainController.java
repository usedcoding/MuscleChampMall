package com.example.MCM;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/")
    public String main1(Model model,
                        Principal principal) {

        Member member = this.memberService.getMember(principal.getName());

        model.addAttribute("member", member);
        return"main";
    }

    @GetMapping("/main")
    public String main2() {
        return"main";
    }
}
