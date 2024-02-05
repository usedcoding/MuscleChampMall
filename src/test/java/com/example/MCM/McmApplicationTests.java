package com.example.MCM;

import com.example.MCM.domain.email.MailService;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.repository.MemberRepository;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class McmApplicationTests {

    @Autowired
    ReviewService reviewService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MailService mailService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("데이터")
    void test1() {
        Member member = Member.builder()
                .username("test2")
                .password("1234")
                .nickname("test2")
                .isDeleted(false)
                .deleted(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .build();
        this.memberRepository.save(member);
    }

    //회원삭제
    @Test
    @DisplayName("삭제")
    void test2() {
        Optional<Member> member = this.memberRepository.findByUsername("test2");
        Member getMember = member.get();
        this.memberService.delete(getMember);
        assertEquals(true, getMember.isDeleted());
    }

    @Test
    @DisplayName("리뷰 생성")
    void t3() {
        Member member = this.memberService.getMember("admin");
        Review review = this.reviewService.createReview(member, "리뷰1", "리뷰1", 5);
    }
}
