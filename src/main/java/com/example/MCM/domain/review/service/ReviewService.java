package com.example.MCM.domain.review.service;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //상품 페이지에 해당 리뷰 리스트 출력
    public List<Review> getReviewList(Product product) {
        return this.reviewRepository.findByProduct(product);
    }

    public Review create(Member author, String content, Double starScore) {
        Review review = Review.builder()
                .author(author)
                .content(content)
                .starScore(starScore)
                .createDate(LocalDateTime.now())
                .build();
        return this.reviewRepository.save(review);
    }

    public void deleteReview(Review review) {
      this.reviewRepository.delete(review);
    }

    public void modifyReview(Review review, String content, Double starScore) {
        Review modifyReview = review.toBuilder()
                .content(content)
                .starScore(starScore)
                .modifyDate(LocalDateTime.now())
                .build();
        this.reviewRepository.save(modifyReview);
    }
}
