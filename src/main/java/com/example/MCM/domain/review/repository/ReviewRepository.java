package com.example.MCM.domain.review.repository;

import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);


    List<Review> findAll();

  Page<Review> findByProduct(Product product, Pageable pageable);

  List<Review> findByProductId(Long productId);
}
