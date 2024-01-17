package com.example.MCM.domain.product.entity;

import com.example.MCM.base.entity.BaseEntity;
import jakarta.persistence.Entity;
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
public class Product extends BaseEntity {

  private String name;

  private Long price;

  private String filePath;

  private String category;

  private String subCategory;
}