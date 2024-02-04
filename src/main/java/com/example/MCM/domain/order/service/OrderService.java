package com.example.MCM.domain.order.service;

import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.order.entity.OrderItem;
import com.example.MCM.domain.order.entity.Orders;
import com.example.MCM.domain.order.repository.OrdRepository;
import com.example.MCM.domain.order.repository.OrderItemRepository;
import com.example.MCM.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrdRepository orderRepository;

  private final OrderItemRepository orderItemRepository;

  public Orders getByBuyerAndProductId(String username, Long productId) {
    return orderRepository.findByBuyerUsernameAndProductId(username, productId);
  }


  @Transactional
  public Orders createOrder(Member buyer, Product product) {

    Orders order = Orders.builder()
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

  public List<Orders> getByBuyerId(Long id) {
    return orderRepository.findByBuyerId(id);
  }

  public List<Orders> getAll() {
    return this.orderRepository.findAll();
  }

  public Page<Orders> getOrders(Pageable pageable) {
    return this.orderRepository.findAll(pageable);
  }
}
