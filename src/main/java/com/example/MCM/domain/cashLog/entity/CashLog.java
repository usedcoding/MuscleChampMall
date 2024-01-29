package com.example.MCM.domain.cashLog.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class CashLog extends BaseEntity {

  private Member member;

  private String username;

  private Product product;

  private String productName;

  private long price;
}
