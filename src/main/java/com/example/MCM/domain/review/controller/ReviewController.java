package com.example.MCM.domain.review.controller;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.member.service.MemberService;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import com.example.MCM.domain.review.ReviewCreateDTO;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final MemberService memberService;

    private final ProductService productService;

    //리뷰 생성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product/{productId}/review/create")
    public String createReview(@PathVariable(value = "productId") Long productId,
                               Model model) {
        model.addAttribute("reviewCreateDTO", new ReviewCreateDTO());
        Product product = this.productService.findById(productId);
        model.addAttribute("productId", productId);
        model.addAttribute("product", product);

        return "review/review_create";
    }

    //리뷰 생성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/product/{productId}/review/create")
    public String createReview(@PathVariable(value = "productId") Long productId,
                               @Valid ReviewCreateDTO reviewCreateDTO,
                               BindingResult bindingResult,
                               Model model,
                               Principal principal) {
        Member member = this.memberService.getMember(principal.getName());
        Product product = this.productService.findById(productId);
        model.addAttribute("productId", productId);
        model.addAttribute("product", product);

        if (bindingResult.hasErrors()) {
            return "review/review_create";
        }

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원이 없습니다.");
        }

        this.reviewService.createReview(member, reviewCreateDTO.getTitle(), reviewCreateDTO.getContent(), reviewCreateDTO.getStarScore(), product);
        productService.updateAverageRating(product);
        return "redirect:/product/"+productId;
    }

    //리뷰 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable(value = "id") Long id, Principal principal) {
        Review review = this.reviewService.getReview(id);

        if (review.getAuthor().getUsername().equals(principal.getName())) {
            this.reviewService.deleteReview(review);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        return "review/review_create";
    }

    //리뷰 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/review/modify/{id}")
    public String modifyReview(@PathVariable(value = "id") Long id, ReviewCreateDTO reviewCreateDTO) {
        Review review = this.reviewService.getReview(id);
        return "review/review_modify";
    }

    //리뷰수정
    @PostMapping("/review/modify/{id}")
    public String modifyReview(@PathVariable(value = "id") Long id, Principal principal, @Valid ReviewCreateDTO reviewCreateDTO, BindingResult bindingResult) {
        Review review = this.reviewService.getReview(id);

        if (bindingResult.hasErrors()) {
            return "review/review_modify";
        }

        if (review.getAuthor().getUsername().equals(principal.getName())) {
            this.reviewService.modifyReview(review, reviewCreateDTO.getTitle(), reviewCreateDTO.getContent(), reviewCreateDTO.getStarScore());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        return "redirect:/product/list";
    }
}