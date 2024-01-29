package com.example.MCM.domain.notice.controller;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.notice.dto.NoticeDto;
import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

  private final NoticeService noticeService;

  private final MemberService memberService;

  @GetMapping("/list")
  public String list(Model model) {

    List<Notice> noticeList = this.noticeService.getAll();

    model.addAttribute("noticeList", noticeList);

    return "notice/list";

  }

  @GetMapping(value = "/{id}")
  public String detail(Model model,
                       @PathVariable("id") Long id) {

    Notice notice = this.noticeService.findById(id);

    model.addAttribute("notice", notice);

    return "notice/detail";
  }

  @GetMapping("/create")
  public String create(NoticeDto noticeDto){
    return "notice/create";
  }

  @PostMapping("/create")
  public String create(@Valid NoticeDto noticeDto,
                       Principal principal,
                       BindingResult bindingResult) {

    if (bindingResult.hasErrors()) return "notice/create";

    Member author = this.memberService.getMember(principal.getName());

    this.noticeService.createValidate(author);

    Notice notice = this.noticeService.create(author, noticeDto);

    return "redirect:/notice/list";

  }

  @GetMapping("/modify/{id}")
  public String modify(@PathVariable("id") Long id,
                       NoticeDto noticeDto) {

    Notice notice = this.noticeService.findById(id);

    this.noticeService.modify(notice, noticeDto);

    return "notice/create";
  }

  @PostMapping("/modify/{id}")
  public String modify(@PathVariable("id") Long id,
                       Principal principal,
                       @Valid NoticeDto noticeDto,
                       BindingResult bindingResult) {

    if (bindingResult.hasErrors()) return "notice/create";

    Member author = this.memberService.getMember(principal.getName());

    Notice notice = this.noticeService.findById(id);

    this.noticeService.modifyValidate(author, notice);

    this.noticeService.modify(notice, noticeDto);

    return String.format("redirect:/notice/{id}", id);

  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id) {

    Notice notice = this.noticeService.findById(id);

    this.noticeService.delete(notice);

    return "redirect:/notice/list";
  }
}
