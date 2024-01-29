package com.example.MCM.domain.cashLog.service;

import com.example.MCM.domain.cashLog.entity.CashLog;
import com.example.MCM.domain.cashLog.repository.CashRepository;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {

  private final CashRepository cashRepository;
  public void addCashLog(Member member, String username, Product product, String name, Integer price) {
    CashLog cashLog = CashLog.builder()
        .member(member)
        .product(product)
        .price(price)
        .username(username)
        .productName(name)
        .build();
    this.cashRepository.save(cashLog);
  }
}
