package com.example.MCM.domain.cartItem.service;

import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.repository.CartItemRepository;
import com.example.MCM.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartItemService {

  private CartItemRepository cartItemRepository;

  public CartItem createCartItem(Product product1, Cart cart, Integer amount) {

    CartItem cartItem = CartItem.builder()
        .product(product1)
        .cart(cart)
        .count(amount)
        .createDate(LocalDateTime.now())
        .build();
    return cartItem;
  }
}
