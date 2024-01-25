package com.example.MCM.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

  private String name;

  private Long price;

  private String content;

  private String imgPath;

  private String description;

  private String imgName;

  private String category;

  private String subCategory;
}
