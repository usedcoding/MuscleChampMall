package com.example.MCM.domain.product.entity;

import com.example.MCM.base.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
}