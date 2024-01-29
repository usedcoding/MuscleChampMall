package com.example.MCM.domain.cart.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.cart.repository.CartRepository;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.cartItem.repository.CartItemRepository;
import com.example.MCM.domain.cartItem.service.CartItemService;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartItemService cartItemService;

  private final CartRepository cartRepository;

  private final CartItemRepository cartItemRepository;

  private final ProductService productService;

  @Transactional
  public void addCart(Product product, Member member, Integer amount){

    Cart cart = this.cartRepository.findByMemberId(member.getId());

    if (cart == null) {
      cart = this.createCart(member);
      cartRepository.save(cart);
    }

    Optional<Product> _product = productService.findProductById(product.getId());

    if (_product.isPresent()) {
      Product product1 = _product.get();

      CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product1.getId());

      if (cartItem == null) {
        cartItem = this.cartItemService.createCartItem(product1, cart, amount);
        cartItemRepository.save(cartItem);
      } else {
        CartItem update = cartItem.toBuilder()
            .cart(cartItem.getCart())
            .product(cartItem.getProduct())
            .count(cartItem.getCount())
            .modifyDate(LocalDateTime.now())
            .build();
        cartItemRepository.save(update);
      }

     cart =  cart.toBuilder()
          .count(cart.getCount() * amount)
          .build();
      cartRepository.save(cart);
    } else {
      throw new DataNotFoundException("상품을 찾을 수 없습니다.");
    }
  }

  public Cart createCart(Member member) {

    Cart cart = Cart.builder()
        .member(member)
        .count(0)
        .createDate(LocalDateTime.now())
        .build();
    return this.cartRepository.save(cart);
  }

}
