package com.example.MCM.domain.product.service;

import com.example.MCM.base.exception.DataNotFoundException.DataNotFoundException;
import com.example.MCM.domain.product.dto.ProductCreateForm;
import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<Product> getAll() {
    return this.productRepository.findAll();
  }

  public Product findById(Long id) {
    Optional<Product> product = this.productRepository.findById(id);
    if (product.isPresent()) {
      return product.get();
    } throw new DataNotFoundException("product not found");
  }

  @Transactional
  public Product create(ProductCreateForm productCreateForm, String filePath) {
    try {
      Product product = Product.builder()
          .name(productCreateForm.getName())
          .price(productCreateForm.getPrice())
          .category(productCreateForm.getCategory())
          .subCategory(productCreateForm.getSubCategory())
          .filePath(filePath)
          .createDate(LocalDateTime.now())
          .build();
      this.productRepository.save(product);
      return product;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("상품 생성에 실패하였습니다.", e);
    }
  }

  @Transactional
  public void modify(Product product, ProductCreateForm productCreateForm) {
    product = product.toBuilder()
        .name(productCreateForm.getName())
        .price(productCreateForm.getPrice())
        .category(productCreateForm.getCategory())
        .subCategory(productCreateForm.getSubCategory())
        .modifyDate(LocalDateTime.now())
        .build();
    this.productRepository.save(product);
  }
}
