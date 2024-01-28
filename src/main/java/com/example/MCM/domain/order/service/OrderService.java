package com.example.MCM.domain.order.service;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.order.entity.Order;
import com.example.MCM.domain.order.entity.OrderItem;
import com.example.MCM.domain.order.repository.OrderItemRepository;
import com.example.MCM.domain.order.repository.OrderRepository;
import com.example.MCM.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final OrderItemRepository orderItemRepository;

  public Order getByBuyerAndProductId(String username, Long productId) {
    return orderRepository.findByBuyerUsernameAndProductId(username, productId);
  }


  @Transactional
  public Order createOrder(Member buyer, Product product) {

    Order order = Order.builder()
        .buyer(buyer)
        .product(product)
        .name(product.getName())
        .createDate(LocalDateTime.now())// 주문 이름은 제품의 제목으로 설정 (예시)
        .isPaid(false)
        .isCanceled(false)
        .isRefunded(false)
        .build();

    OrderItem orderItem = OrderItem.builder()
        .order(order)
        .product(product)
        .isPaid(true)
        .payDate(LocalDateTime.now())
        .payPrice(product.getPrice())
        .build();

    orderItemRepository.save(orderItem);


    return orderRepository.save(order);
  }

  public List<Order> getByBuyerId(Long id) {
    return orderRepository.findByBuyerId(id);
  }
}