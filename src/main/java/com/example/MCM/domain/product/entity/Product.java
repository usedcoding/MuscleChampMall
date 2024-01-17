package com.example.MCM.domain.product.entity;

import com.example.MCM.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
public class Product extends BaseEntity {

  private String name;

  private Long price;

  private String fileName;

  private String filePath;

  private String category;

  private String subCategory;
}