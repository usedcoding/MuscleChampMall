package com.example.MCM.domain.cartItem.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.repository.CartItemRepository;
import com.example.MCM.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

  private final CartItemRepository cartItemRepository;

  // 생성자를 통한 의존성 주입을 사용했으므로, @Autowired 어노테이션이 필요하지 않습니다.
  // RequiredArgsConstructor 어노테이션을 사용한 경우 생성자가 자동으로 생성되기 때문에, 여기서는 생성자가 이미 있습니다.

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

}
