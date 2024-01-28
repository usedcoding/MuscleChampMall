package com.example.MCM.domain.cart.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.member.entity.Member;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Cart extends BaseEntity {

  private Member member;

  private int count;

  private List<CartItem> cartItemList;
}
