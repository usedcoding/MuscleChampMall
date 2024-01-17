package com.example.MCM.domain.product.service;

import com.example.MCM.domain.product.entity.Product;
import com.example.MCM.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<Product> getAll() {
    return this.productRepository.findAll();
  }
}
