package com.example.MCM.domain.order.controller;

import com.example.MCM.domain.order.repository.OrderItemRepository;
import com.example.MCM.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

}
