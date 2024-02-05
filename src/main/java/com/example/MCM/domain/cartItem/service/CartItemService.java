package com.example.MCM.domain.cartItem.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.repository.CartItemRepository;
import com.example.MCM.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

  public void deleteCartItem(Long cartItemId){
    Optional<CartItem> optionalCartItem = this.cartItemRepository.findById(cartItemId);
    if (optionalCartItem.isPresent()) {
        CartItem cartItem = optionalCartItem.get();
        this.cartItemRepository.delete(cartItem);
    } else {
      throw new DataNotFoundException("장바구니에 담긴 상품을 찾을 수 없습니다.");
    }
  }

  public List<CartItem> getAll(Cart cart) {
    return this.cartItemRepository.findAll();
  }

  public void cartItemDelete(Long itemId) {
    CartItem cartItem = this.cartItemRepository.findCartItemById(itemId);
    this.cartItemRepository.delete(cartItem);
  }
}
