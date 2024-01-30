package com.example.MCM.domain.review.controller;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.review.ReviewCreateDTO;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final MemberService memberService;

    //리뷰 생성
    @GetMapping("/create")
    public String createReview() {
        return "review_create";
    }

    //리뷰 생성
    @PostMapping("/create")
    public String createReview(@Valid ReviewCreateDTO reviewCreateDTO, BindingResult bindingResult, Principal principal) {
        Member member = this.memberService.getMember(principal.getName());

        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
        }

        this.reviewService.createReview(member, reviewCreateDTO.getTitle(), reviewCreateDTO.getContent(), reviewCreateDTO.getStarScore());
        return "redirect:/";
    }

    //리뷰 삭제
    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable(value = "id") Long id, Principal principal) {
        Review review = this.reviewService.getReview(id);

        if (review.getAuthor().equals(principal.getName())) {
            this.reviewService.deleteReview(review);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        return "redirect:/";
    }

    //회원수정
    @PostMapping("/modify/{id}")
    public String modifyReview(@PathVariable(value = "id") Long id, Principal principal, @Valid ReviewCreateDTO reviewCreateDTO, BindingResult bindingResult) {
        Review review = this.reviewService.getReview(id);

        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }

        if(review.getAuthor().equals(principal.getName())) {
            this.reviewService.modifyReview(review, reviewCreateDTO.getTitle(), reviewCreateDTO.getContent(), reviewCreateDTO.getStarScore());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        return "redirect:/";

        //수정 내용 작성 페이지 새로 생성? 아니면 리뷰 작성 페이지 재활용
    }
}
