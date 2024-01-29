package com.example.MCM.domain.cartItem.repository;

import com.example.MCM.domain.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  CartItem findByCartIdAndProductId(Long id, Long id1);
}
