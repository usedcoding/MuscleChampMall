package com.example.MCM.domain.order.entity;


import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Ord extends BaseEntity {

  @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE) // CascadeType.REMOVE 추가
  private Member buyer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<OrderItem> orderItemList;

  @ManyToOne
  private Product product;

  private String name;

  private boolean isPaid;

  private boolean isCanceled;

  private boolean isRefunded;
}
