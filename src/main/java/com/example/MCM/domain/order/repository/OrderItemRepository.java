package com.example.MCM.domain.order.repository;

import com.example.MCM.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  List<OrderItem> findByIsPaidTrueAndPayDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

}
