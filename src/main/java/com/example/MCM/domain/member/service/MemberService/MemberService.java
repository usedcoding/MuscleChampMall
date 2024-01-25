package com.example.MCM.domain.member.service.MemberService;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberService.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;


  public Member findByUsername(String name) {
    return this.memberRepository.findByUsername(name);
  }
}
