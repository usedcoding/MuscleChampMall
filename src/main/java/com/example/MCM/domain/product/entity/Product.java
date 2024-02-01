package com.example.MCM.domain.product.entity;

import com.example.MCM.base.entity.BaseEntity;
import com.example.MCM.domain.cartItem.entity.CartItem;
import com.example.MCM.domain.member.entity.Member;
import com.example.MCM.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
public class Product extends BaseEntity {

  private String name;

  private Long price;

  private String description;

  private String content;

  @ElementCollection
  @CollectionTable(name = "product_imgPath", joinColumns = @JoinColumn(name = "product_id"))
  private List<String> imgPath;

  @ElementCollection
  @CollectionTable(name = "product_imgName", joinColumns = @JoinColumn(name = "product_id"))
  private List<String> imgName;

  private String category;

  private String subCategory;

  private Long viewCount;

  @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<CartItem> cartItemList;

  @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Review> reviewList;

  @ManyToOne
  private Member author;

}