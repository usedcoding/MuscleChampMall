package com.example.MCM.domain.notice.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.notice.dto.NoticeDto;
import com.example.MCM.domain.notice.entity.Notice;
import com.example.MCM.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

  public void createValidate(Member author) {

    if (!author.getRole().equals("ADMIN")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "공지사항 작성 권한이 없습니다.");
    }
  }

  public Notice create(Member author, NoticeDto noticeDto) {

    Notice notice = Notice.builder()
        .subject(noticeDto.getSubject())
        .content(noticeDto.getContent())
        .author(author)
        .createDate(LocalDateTime.now())
        .build();

    return this.noticeRepository.save(notice);

  }
}
