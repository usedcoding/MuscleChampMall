package com.example.MCM.domain.review.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Review extends BaseEntity {

  private String content;

  private Double starScore;

  @ManyToOne
  private Member author;

  @ManyToOne
  private Product product;
}
