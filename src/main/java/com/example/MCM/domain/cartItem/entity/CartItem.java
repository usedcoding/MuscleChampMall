package com.example.MCM.domain.cartItem.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.cart.entity.Cart;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class CartItem extends BaseEntity {

  @ManyToOne
  private Product product;

  @ManyToOne
  private Cart cart;

  private int count;

  private void addCount(int count) {
    this.count += count;
  };
}
