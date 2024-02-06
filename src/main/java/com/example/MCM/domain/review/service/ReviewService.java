package com.example.MCM.domain.review.service;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.review.entity.Review;
import com.example.MCM.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //상품 페이지에 해당 리뷰 리스트 출력
    public List<Review> getReviewList(Product product) {
        return this.reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewAll(){
        return this.reviewRepository.findAll();
    }
    public Review getReview(Long id) {
        Optional<Review> review =  this.reviewRepository.findById(id);

        return review.get();
    }

    //리뷰 생성
    @Transactional
    public Review createReview(Member author, String title, String content, double starScore, Product product) {
        Review review = Review.builder()
                .author(author)
                .content(content)
                .title(title)
                .starScore(starScore)
                .product(product)
                .createDate(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        return review;
    }

    //리뷰 삭제
    public void deleteReview(Review review) {
        this.reviewRepository.delete(review);
    }

    //리뷰 수정
    @Transactional
    public void modifyReview(Review review, String title, String content, double starScore) {
        Review modifyReview = review.toBuilder()
                .title(title)
                .content(content)
                .starScore(starScore)
                .modifyDate(LocalDateTime.now())
                .build();
        this.reviewRepository.save(modifyReview);
    }

    public Page<Review> getByproduct(Product product, Pageable pageable) {
        return this.reviewRepository.findByProduct(product, pageable);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return this.reviewRepository.findByProductId(productId);
    }

}
