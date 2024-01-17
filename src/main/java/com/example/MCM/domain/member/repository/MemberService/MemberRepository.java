package com.example.MCM.domain.member.repository.MemberService;

import com.example.MCM.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Member findByUsername(String name);
}
