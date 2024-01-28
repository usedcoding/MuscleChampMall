package com.example.MCM.domain.member.repository;

import com.example.MCM.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
//    Member findByUsernameAndIsDeleted(String username, boolean isDeleted);
//    탈퇴된 회원인지 확인하고 불러오기

    Member findByEmail(String email);

    Member findByEmailAndPhoneNumber(String email, String phoneNumber);
}
