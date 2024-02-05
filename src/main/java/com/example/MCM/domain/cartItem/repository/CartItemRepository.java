package com.example.MCM.domain.cartItem.repository;

import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  CartItem findByCartIdAndProductId(Long id, Long id1);

  CartItem findCartItemById(Long itemId);

  List<CartItem> findAllByCart(Cart cart);
}
