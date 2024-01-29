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

    //이메일로 찾기
    Member findByEmail(String email);

    //이메일과 전화번호로 찾기

    Member findByEmailAndPhoneNumber(String email, String phoneNumber);

    Member findByEmailAndPhoneNumberAndUsername(String email, String phoneNumber, String username);
}
