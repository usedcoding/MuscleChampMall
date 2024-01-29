package com.example.MCM.domain.order.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

  @ManyToOne(fetch = LAZY)
  private Ord order;

  private LocalDateTime payDate;

  @ManyToOne
  private Product product;

  private Long payPrice;

  private boolean isPaid;
}