package com.example.MCM.domain.order.repository;

import com.example.MCM.domain.order.entity.Ord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdRepository extends JpaRepository<Ord, Long> {
  Ord findByBuyerUsernameAndProductId(String username, Long productId);

  List<Ord> findByBuyerId(Long id);
}
