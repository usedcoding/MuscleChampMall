package com.example.MCM.domain.notice.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;

  public List<Notice> getAll() {
    return this.noticeRepository.findAll();
  }

  public Notice findById(Long id) {
    Optional<Notice> _notice = this.noticeRepository.findById(id);
    if (_notice.isPresent()) {
      return _notice.get();
    } throw new DataNotFoundException("notice not found");
  }
}
