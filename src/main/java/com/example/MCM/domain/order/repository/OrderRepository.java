package com.example.MCM.domain.order.repository;

import com.example.MCM.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Order findByBuyerUserNameAndProductId(String userName, Long productId);

  List<Order> findByBuyerId(Long id);
}
