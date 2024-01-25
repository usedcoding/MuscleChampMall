package com.example.MCM.domain.order.service;

import com.example.MCM.domain.order.repository.OrderItemRepository;
import com.example.MCM.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final OrderItemRepository orderItemRepository;
}
