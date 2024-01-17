package com.example.MCM.domain.product.dto;

import lombok.Getter;

@Getter
public class ProductCreateForm {

  private String subject;

  private String content;

  private Long price;

  private String fileName;

  private String filePath;

  private String category;

  private String subCategory;
}
