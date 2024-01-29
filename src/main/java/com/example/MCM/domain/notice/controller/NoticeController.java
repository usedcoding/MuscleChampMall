package com.example.MCM.domain.notice.controller;

import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

  private final NoticeService noticeService;

  @GetMapping("/list")
  public String list(Model model) {

    List<Notice> noticeList = this.noticeService.getAll();

    model.addAttribute("noticeList", noticeList);

    return "notice/list";

  }
}
