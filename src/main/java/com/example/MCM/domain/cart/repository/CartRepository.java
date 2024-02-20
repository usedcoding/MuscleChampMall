package com.example.MCM.domain.cart.repository;

import com.example.MCM.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByMemberId(Long id);
}
