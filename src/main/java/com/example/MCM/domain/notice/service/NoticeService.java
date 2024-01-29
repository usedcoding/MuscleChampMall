package com.example.MCM.domain.notice.service;

import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;

  public List<Notice> getAll() {
    return this.noticeRepository.findAll();
  }
}
