package com.example.MCM.domain.order.repository;

import com.example.MCM.domain.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdRepository extends JpaRepository<Orders, Long> {
  Orders findByBuyerUsernameAndProductId(String username, Long productId);

  List<Orders> findByBuyerId(Long id);

}
